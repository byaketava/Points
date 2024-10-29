package by.byak.points

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var animatedIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        animatedIcon = findViewById(R.id.animatedIcon)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Скрываем нижнее меню навигации в начале
        bottomNavigationView.visibility = View.GONE

        // Проверяем, аутентифицирован ли пользователь
        if (auth.currentUser != null) {
            // Если пользователь аутентифицирован, сразу загружаем ProfileFragment
            loadFragment(ProfileFragment())
            animatedIcon.visibility = View.GONE // Скрываем иконку, если она есть
        } else {
            // Если не аутентифицирован, выполняем анимацию
            performAnimation()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.navigation_create_route -> {
                    loadFragment(CreateRouteFragment())
                    true
                }
                R.id.navigation_routes -> {
                    loadFragment(RoutesFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        bottomNavigationView.visibility = View.VISIBLE // Показываем навигационное меню при загрузке фрагмента
    }

    private fun performAnimation() {
        // Скрываем нижнее меню навигации во время анимации
        bottomNavigationView.visibility = View.GONE

        animatedIcon.visibility = View.VISIBLE // Показываем иконку анимации

        // Анимация падения сверху вниз
        val screenHeight = resources.displayMetrics.heightPixels
        val dropAnimation = ObjectAnimator.ofFloat(
            animatedIcon, "translationY", -420f, screenHeight / 2f - 180f
        )
        dropAnimation.duration = 2000
        dropAnimation.interpolator = AccelerateInterpolator()

        // Анимация увеличения и исчезновения
        val scaleUpX = ObjectAnimator.ofFloat(animatedIcon, "scaleX", 1f, 3f)
        val scaleUpY = ObjectAnimator.ofFloat(animatedIcon, "scaleY", 1f, 3f)
        val fadeOut = ObjectAnimator.ofFloat(animatedIcon, "alpha", 1f, 0f)

        scaleUpX.duration = 1000
        scaleUpY.duration = 1000
        fadeOut.duration = 1000

        val animatorSet = AnimatorSet()

        dropAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                animatorSet.play(scaleUpX).with(scaleUpY).before(fadeOut)
                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator) {
                        // После завершения анимации проверяем, есть ли текущий пользователь
                        if (auth.currentUser == null) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish() // Закрываем MainActivity
                        } else {
                            // Если пользователь уже авторизован, загружаем ProfileFragment
                            loadFragment(ProfileFragment())
                        }
                        animatedIcon.visibility = View.GONE // Скрываем иконку после анимации
                    }

                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                animatorSet.start()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        dropAnimation.start()
    }
}
