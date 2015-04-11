package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    public static final int RESULT_IMAGE_DOWNLOAD_FAILED = 1;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
        final Intent intent = getIntent();
        final Uri imageUri = intent.getData();

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                final Uri downloadedImageUri = DownloadUtils.downloadImage(DownloadImageActivity.this, imageUri);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadedImageUri != null) {
                            Intent result = new Intent();
                            result.setData(downloadedImageUri);
                            setResult(RESULT_OK, result);
                        } else {
                            setResult(RESULT_IMAGE_DOWNLOAD_FAILED);
                        }
                        finish();
                    }
                });

            }
        });

    }
}
