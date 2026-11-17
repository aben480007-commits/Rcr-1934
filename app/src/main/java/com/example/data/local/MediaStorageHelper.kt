package com.example.data.local

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

sealed class LogoPersistResult {
    data class Success(val relativePath: String) : LogoPersistResult()
    data object InvalidImage : LogoPersistResult()
    data object WriteFailed : LogoPersistResult()
}

object MediaStorageHelper {

    private const val MEDIA_ROOT_DIR = "rcr_media"
    private const val LOGOS_SUBDIR = "logos"

    suspend fun persistPickedMedia(context: Context, sourceUri: Uri, prefix: String = "media"): String? =
        withContext(Dispatchers.IO) {
            runCatching {
                val resolver = context.contentResolver
                val mimeType = resolver.getType(sourceUri).orEmpty()
                val ext = when {
                    mimeType.contains("png", ignoreCase = true) -> "png"
                    mimeType.contains("webp", ignoreCase = true) -> "webp"
                    mimeType.contains("mp4", ignoreCase = true) -> "mp4"
                    else -> "jpg"
                }
                val dir = File(context.filesDir, MEDIA_ROOT_DIR).apply { mkdirs() }
                val dest = File(dir, "${prefix}_${UUID.randomUUID()}.$ext")
                resolver.openInputStream(sourceUri)?.use { input ->
                    dest.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: return@withContext null
                dest.absolutePath
            }.getOrNull()
        }

    suspend fun validateImageUri(context: Context, uri: Uri): Boolean =
        withContext(Dispatchers.IO) {
            runCatching {
                val resolver = context.contentResolver
                val mime = resolver.getType(uri)
                if (!mime.isNullOrBlank() && !mime.startsWith("image/", ignoreCase = true)) {
                    return@runCatching false
                }
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                resolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream, null, bounds)
                } ?: return@runCatching false
                bounds.outWidth > 0 && bounds.outHeight > 0
            }.getOrDefault(false)
        }

    suspend fun persistValidatedLogo(
        context: Context,
        sourceUri: Uri,
        prefix: String
    ): LogoPersistResult = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val declaredMime = resolver.getType(sourceUri)
        if (!declaredMime.isNullOrBlank() && !declaredMime.startsWith("image/", ignoreCase = true)) {
            return@withContext LogoPersistResult.InvalidImage
        }

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        val isDecodable = runCatching {
            resolver.openInputStream(sourceUri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, bounds)
            } != null || (bounds.outWidth > 0 && bounds.outHeight > 0)
        }.getOrDefault(false)

        if (!isDecodable || bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            return@withContext LogoPersistResult.InvalidImage
        }

        val effectiveMime = (bounds.outMimeType ?: declaredMime).orEmpty()
        val ext = when {
            effectiveMime.contains("png", ignoreCase = true) -> "png"
            effectiveMime.contains("webp", ignoreCase = true) -> "webp"
            else -> "jpg"
        }

        val safePrefix = prefix.lowercase().replace(Regex("[^a-z0-9_-]"), "_").ifBlank { "logo" }
        val logosDir = File(File(context.filesDir, MEDIA_ROOT_DIR), LOGOS_SUBDIR).apply { mkdirs() }
        val destFile = File(logosDir, "${safePrefix}_${UUID.randomUUID()}.$ext")

        val copied = runCatching {
            resolver.openInputStream(sourceUri)?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return@runCatching false
            destFile.exists() && destFile.length() > 0L
        }.getOrDefault(false)

        if (!copied) {
            runCatching { destFile.delete() }
            return@withContext LogoPersistResult.WriteFailed
        }

        val verifyBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        runCatching { BitmapFactory.decodeFile(destFile.absolutePath, verifyBounds) }
        if (verifyBounds.outWidth <= 0 || verifyBounds.outHeight <= 0) {
            runCatching { destFile.delete() }
            return@withContext LogoPersistResult.InvalidImage
        }

        LogoPersistResult.Success("$MEDIA_ROOT_DIR/$LOGOS_SUBDIR/${destFile.name}")
    }

    fun resolveStoredMediaFile(context: Context, storedPath: String): File? {
        if (storedPath.isBlank()) return null
        val candidate = if (storedPath.startsWith("/")) {
            File(storedPath)
        } else {
            File(context.filesDir, storedPath)
        }
        return candidate.takeIf { it.exists() && it.isFile && it.length() > 0L }
    }

    fun isReadableImageFile(context: Context, storedPath: String): Boolean {
        val file = resolveStoredMediaFile(context, storedPath) ?: return false
        return runCatching {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.absolutePath, bounds)
            bounds.outWidth > 0 && bounds.outHeight > 0
        }.getOrDefault(false)
    }

    fun deleteManagedMediaIfUnreferenced(
        context: Context,
        oldPath: String?,
        activePaths: Set<String>
    ) {
        if (oldPath.isNullOrBlank()) return
        if (oldPath in activePaths) return
        val file = resolveStoredMediaFile(context, oldPath) ?: return
        val managedRoot = File(context.filesDir, MEDIA_ROOT_DIR).canonicalPath
        runCatching {
            if (file.canonicalPath.startsWith(managedRoot)) {
                file.delete()
            }
        }
    }
}
