# SpyroTheDragon-App

Aplicación desarrollada en Android Studio basada en el videojuego *Spyro the Dragon*. Esta app permite explorar personajes, coleccionables,mundos y descubrir dos Easter Eggs ocultos.

---

## **Características Principales**
- Explora personajes icónicos de *Spyro*.
- Descubre los coleccionables que hay en el juego.
- Navegación entre secciones. Implementamos un DrawerLayout con un menú lateral desde donde pondemos acceder a la diferentes secciones:    Personajes, coleccionables y mundos.
- El sistema de navegación se configuró con NavGraph.
- Se ha creado una guía interactiva que explica a modo de tutorial la aplicación paso a paso.
- Sonidos en eventos clave como cambios de sección de la guía.
- **Easter Egg de video**: Se activa tras hacer clic cuatro veces en las gemas, reproduciendo un video temático a pantalla completa.
- **Easter Egg de animación**: Se activa con una pulsación larga sobre *Spyro*, mostrando una animación de fuego con `Canvas`.

---

## **Tecnologías Utilizadas**
- **Android Studio `LadyBug | 2024.2.2`**
- **Kotlin para el Gradle & Java para las clases de los fragments,adapters etc**
- **Diferentes Layouts como ConstraintLayout y RecyclerView** para la estructuración de la UI.
- **Canvas API** para animaciones personalizadas.
- **MediaPlayer** para reproducción de sonidos.

---

## **Cómo Ejecutar la Aplicación**
1. Clona el repositorio:
   ```bash
   git clone https://github.com/larnahid/SpyroTheDragon-App.git
   ```
2. Abre el proyecto en **Android Studio**.
3. Conecta un emulador o dispositivo físico.
4. Ejecuta la aplicación desde el botón *Run*.

---

## **Conclusiones del Desarrollador**
Este proyecto ha permitido explorar diversas funcionalidades de desarrollo Android.Hemos mejorado nuestros conocimientos sobre la navegación dentro de la App. También se hace uso de Listener de múltiples botones y objetos para que la App tenga funcionalidad y haga lo que se le pide. Se han usado tanto reproductor de medios mediaplayer como Canvas para realizar animaciones.
Por último mencionar que por mi parte he aprendido a manejar mejor tanto los Layouts de los fragments, así como el manejo y modificación de los adapters para los easter eggs.
