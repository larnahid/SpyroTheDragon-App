package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FireAnimationView extends View {

    private Paint paint;
    private boolean isAnimating = false;

    public FireAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isAnimating) {
            // Dibujar llamas
            paint.setColor(Color.RED);
            canvas.drawOval(100, 100, 300, 400, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawOval(120, 120, 280, 380, paint);

            paint.setColor(Color.RED);
            canvas.drawOval(140, 140, 260, 360, paint);
        }
    }

    public void startFireAnimation() {
        isAnimating = true;
        invalidate();

        // Detener animación
        postDelayed(() -> {
            isAnimating = false;
            invalidate();
        }, 1500);
    }
}
