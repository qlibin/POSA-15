package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

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

    public static final String DOWNLOAD_IMAGE_URL = "imageUrl";
    public static final String DOWNLOADED_IMAGE_URL = "imageUrl";

    public static final int RESULT_IMAGE_DOWNLOAD_FAILED = 1;

    private Handler mHandler;

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
        mHandler = new Handler();

        // Get the URL associated with the Intent data.
        final Intent intent = getIntent();
        final Uri imageUri = intent.getParcelableExtra(DOWNLOAD_IMAGE_URL);

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                final Uri downloadedImageUri = DownloadUtils.downloadImage(DownloadImageActivity.this, imageUri);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (downloadedImageUri != null) {
                            Intent result = new Intent();
                            result.putExtra(DOWNLOADED_IMAGE_URL, downloadedImageUri);
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
