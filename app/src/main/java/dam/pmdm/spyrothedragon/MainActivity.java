package dam.pmdm.spyrothedragon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ActionBarDrawerToggle toggle;

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private Boolean needToStartGuide = true;

    private View lastHighlightedItem = null;

    private int stepIndex = -1;
    private final String[] guideSteps = {
            "Aquí puedes ver todos los personajes de Spyro.",
            "Aquí puedes explorar los mundos disponibles.",
            "Aquí encontrarás los coleccionables.",
            "Este icono te dará información adicional.",
            "¡Has terminado la guía! Pulsa para cerrar."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        guideBinding = binding.includeLayout;

        // Configuración del  toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Controlador de la navegación
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.navHostFragment);


        navController = navHostFragment.getNavController();

        configureToggleMenu();

        // Configurar Toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configureNavigation();

        guideBinding.exitGuide.setOnClickListener(this::onExitGuide);

        // Verificar si la guía ya se ha mostrado
        SharedPreferences sharedPreferences = getSharedPreferences("SpyroPrefs", MODE_PRIVATE);
        boolean guideCompleted = sharedPreferences.getBoolean("guideCompleted", false);
        if (!guideCompleted) {
            guideBinding.guideLayout.setVisibility(View.VISIBLE);
        }


        // Botón para iniciar la guía
        guideBinding.btnStart.setOnClickListener(v -> {
            guideBinding.pantallaBienvenida.setVisibility(View.GONE);
            guideBinding.guideStepsContainer.setVisibility(View.VISIBLE);
            guideBinding.guideLayout.setVisibility(View.VISIBLE); // Asegurar que se vea la guía

            stepIndex = 0;
            guideBinding.textStep.setText(guideSteps[stepIndex]);
            updatePulsePosition(stepIndex);


            /*navigationView.setEnabled(true);
            navigationView.setClickable(true);*/

            drawerLayout.openDrawer(GravityCompat.START);

            navigationView.setCheckedItem(R.id.navigation_characters);
            navController.navigate(R.id.navigation_characters);

            drawerLayout.postDelayed(() -> {
                drawerLayout.closeDrawers();
            }, 3000);
        });



        // Botón siguiente paso
        guideBinding.btnNextStep.setOnClickListener(v -> {
            playSound(R.raw.button_click,1);  // 🔊 Sonido al pulsar siguiente
            nextGuideStep();
        });

    }

    private void onExitGuide(View view) {
        playSound(R.raw.exit_guide, 1);
        needToStartGuide = false;
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        guideBinding.guideLayout.setVisibility(View.GONE);
        binding.drawerLayout.close();
    }

    private void configureNavigation() {
        // Configuramos la navegación a partir del navController

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(menuItem -> {

            int id = menuItem.getItemId();

            if (id == R.id.navigation_characters) {
                navController.navigate(R.id.navigation_characters);
            } else if (id == R.id.navigation_worlds) {
                navController.navigate(R.id.navigation_worlds);
            } else if (id == R.id.navigation_collectibles) {
                navController.navigate(R.id.navigation_collectibles);
            } else {
                return false;
            }

            drawerLayout.closeDrawers(); // Ciweeo wl menú lateral
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar clics en el icono del menú
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureToggleMenu() {
        // Configurar el ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        if (navHostFragment != null) {
            NavController navController = NavHostFragment.findNavController(navHostFragment);
            return NavigationUI.navigateUp(navController, binding.drawerLayout) || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

    private void nextGuideStep() {
        // Manejador de los pasos de la guía
        if (stepIndex >= 0) {
            hideBocadilloWithAnimation(() -> {
                stepIndex++;

                if (stepIndex < guideSteps.length) {
                    guideBinding.textStep.setText(guideSteps[stepIndex]);

                    guideBinding.bocadilloContainer.postDelayed(() -> {
                        showBocadilloWithAnimation();
                        openDrawerAndNavigate(stepIndex);
                        animateMenuItem(stepIndex);

                    }, 300);
                } else {
                    guideBinding.guideLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = getSharedPreferences("SpyroPrefs", MODE_PRIVATE).edit();
                    editor.putBoolean("guideCompleted", true);
                    editor.apply();
                    return;
                }
            });
        } else {
            stepIndex++;
            guideBinding.textStep.setText(guideSteps[stepIndex]);
            showBocadilloWithAnimation();
            openDrawerAndNavigate(stepIndex);
        }
    }

    private void hideBocadilloWithAnimation(Runnable onComplete) {
        // Animación para ocultar el bocadillo
        Animation fadeOutScale = AnimationUtils.loadAnimation(this, R.anim.fade_out_scale);

        fadeOutScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                guideBinding.bocadilloContainer.setVisibility(View.GONE);
                guideBinding.bocadilloContainer.setScaleX(1.0f);
                guideBinding.bocadilloContainer.setScaleY(1.0f);
                guideBinding.bocadilloContainer.getParent().requestLayout();

                if (onComplete != null) {
                    onComplete.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        guideBinding.bocadilloContainer.startAnimation(fadeOutScale);
    }

    private void openDrawerAndNavigate(int step) {
        //Navegamos según el paso de la guía al la pantalla del menú correspondiente
        runOnUiThread(() -> {
            if (step < 3) {
                drawerLayout.openDrawer(GravityCompat.START);
            }

            switch (step) {
                case 0:
                    navigationView.setCheckedItem(R.id.navigation_characters);
                    navController.navigate(R.id.navigation_characters);
                    break;
                case 1:
                    navigationView.setCheckedItem(R.id.navigation_worlds);
                    navController.navigate(R.id.navigation_worlds);
                    break;
                case 2:
                    navigationView.setCheckedItem(R.id.navigation_collectibles);
                    navController.navigate(R.id.navigation_collectibles);
                    break;
                case 3:
                    animateMenuItem(3);
                    return;
            }

            if (step < 3) {
                drawerLayout.postDelayed(() -> drawerLayout.closeDrawers(), 5000);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    private View getMenuItemView(int step) {
        if (step == 3) {
            View infoIcon = findViewById(R.id.action_info); // 🔥 Buscar directamente en la Toolbar
            return infoIcon;
        }

        int menuItemId;
        switch (step) {
            case 0:
                menuItemId = R.id.navigation_characters;
                break;
            case 1:
                menuItemId = R.id.navigation_worlds;
                break;
            case 2:
                menuItemId = R.id.navigation_collectibles;
                break;
            default:
                return null;
        }

        MenuItem menuItem = navigationView.getMenu().findItem(menuItemId);
        if (menuItem == null) return null;

        return findViewById(menuItem.getItemId());
    }




    private void animateMenuItem(int step) {
        final View menuItemView = getMenuItemView(step);
        if (menuItemView == null) return;

        if (step == 3) {

            // Anima el menú de ayuda
            menuItemView.bringToFront();
            menuItemView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_background);
            blinkAnimation.setAnimationListener(new Animation.AnimationListener() {
                int count = 0;

                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    count++;
                    if (count < 5) { // Repito 4 veces
                        menuItemView.startAnimation(blinkAnimation);
                    } else {
                        // Restauro el color original
                        menuItemView.setBackgroundResource(0);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            menuItemView.startAnimation(blinkAnimation);
        } else {
            // Animaciones de la Toolbar
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            menuItemView.startAnimation(blinkAnimation);
        }

        lastHighlightedItem = menuItemView; // Guardar el elemento resaltado

        playSound(R.raw.menu_blink, 2);  // Sonido asignado parpadeo
    }

    private void updatePulsePosition(int step) {
        animateMenuItem(step);
    }

    private void showBocadilloWithAnimation() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Aparecer bocadillo con Fade In
        guideBinding.bocadilloContainer.startAnimation(fadeIn);
        guideBinding.bocadilloContainer.setVisibility(View.VISIBLE);
    }

    private MediaPlayer mediaPlayer;

    private void playSound(int soundResource, int repeatCount) {

        // Metodo que usa mediaplayer para reproducir sonidos n veces
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, soundResource);
        mediaPlayer.setLooping(false); // No queremos bucle infinito
        mediaPlayer.setVolume(1.0f, 1.0f); // Volumen

        if (repeatCount > 1) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                int count = 1;

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count < repeatCount) {
                        count++;
                        mediaPlayer.start(); // Reproducir de nuevo
                    } else {
                        mediaPlayer.release(); // Liberar al finalizar todas las repeticiones
                    }
                }
            });
        }

        mediaPlayer.start();
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

}
