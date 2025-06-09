package app.revanced.patches.shared.misc.spoof

import app.revanced.patcher.patch.*
import app.revanced.util.getNode
import org.w3c.dom.Element


fun signatureSpoofPatch(
    block: ResourcePatchBuilder.() -> Unit = {},
    signature: String
): ResourcePatch {
    return resourcePatch(
        name = "Spoof app signature",
        description = "Spoofs the app signature via the \"fake-signature\" meta key. " +
            "Only supported on a patched rom.",
        use = false,
    ) {
        block()
        finalize {
            document("AndroidManifest.xml").use { document ->
                val manifest = document.getNode("manifest") as Element

                val fakeSignaturePermission = document.createElement("uses-permission")
                fakeSignaturePermission.setAttribute("android:name", "android.permission.FAKE_PACKAGE_SIGNATURE")
                manifest.appendChild(fakeSignaturePermission)

                val application = document.getNode("application") ?: {
                    val child = document.createElement("application")
                    manifest.appendChild(child)
                    child
                } as Element;

                val fakeSignatureMetadata = document.createElement("meta-data")
                fakeSignatureMetadata.setAttribute("android:name", "fake-signature")
                fakeSignatureMetadata.setAttribute("android:value", signature)
                application.appendChild(fakeSignatureMetadata)

            }
        }
    }
}