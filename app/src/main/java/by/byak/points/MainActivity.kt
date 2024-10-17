package by.byak.points

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val animatedIcon = findViewById<ImageView>(R.id.animatedIcon)

        // Анимация падения сверху вниз
        val screenHeight = resources.displayMetrics.heightPixels
        val dropAnimation =
            ObjectAnimator.ofFloat(animatedIcon, "translationY", -420f, screenHeight / 2f - 180f)
        dropAnimation.duration = 2000
        dropAnimation.interpolator = AccelerateInterpolator()

        // Анимация увеличения и исчезновения
        val scaleUpX = ObjectAnimator.ofFloat(animatedIcon, "scaleX", 1f, 3f) // Увеличение по X
        val scaleUpY = ObjectAnimator.ofFloat(animatedIcon, "scaleY", 1f, 3f) // Увеличение по Y
        val fadeOut = ObjectAnimator.ofFloat(animatedIcon, "alpha", 1f, 0f) // Прозрачность

        // Устанавливаем длительности анимаций
        scaleUpX.duration = 1000 // Увеличение длится 1 секунду
        scaleUpY.duration = 1000 // Увеличение длится 1 секунду
        fadeOut.duration = 1000 // Исчезновение длится 0.5 секунды

        val animatorSet = AnimatorSet()

        // Слушатель для смены иконки и проверки пользователя после завершения анимации
        dropAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Запускаем анимацию увеличения и исчезновения после завершения падения
                animatorSet.play(scaleUpX).with(scaleUpY).before(fadeOut)
                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator) {
                        // Проверяем, есть ли текущий пользователь после анимации
                        if (auth.currentUser == null) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                        }
                        finish()
                    }

                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                animatorSet.start() // Запускаем набор анимаций
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        // Запускаем анимацию падения
        dropAnimation.start()
    }
}