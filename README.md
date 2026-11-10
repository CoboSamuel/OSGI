
Sistema modular OSGi con Apache Karaf
Autor: Samuel Cobo
Carrera: Ingeniería de Software
Asignatura: Diseño y Arquitectura de Software
Repositorio: github.com/CoboSamuel/OSGI
Estado: Implementación y pruebas manuales completadas.

1. ¿De qué trata este proyecto?
Este proyecto consiste en crear una aplicación modular en Java con OSGi y ejecutarla dentro de Apache Karaf. La idea es comprobar, mediante una práctica sencilla, que un sistema puede estar formado por módulos relacionados que se comuniquen sin quedar completamente dependientes entre sí.

Para hacerlo desarrollamos dos bundles (módulos OSGi): uno ofrece un servicio de saludos y el otro lo utiliza. Después comprobamos qué ocurre al detener y volver a iniciar cada módulo mientras Karaf permanece encendido.

El objetivo no era únicamente obtener un mensaje en pantalla. Lo importante era demostrar el ciclo de vida independiente de los bundles, la comunicación mediante servicios y el bajo acoplamiento.

2. Tecnologías y herramientas utilizadas
Herramienta	Uso dentro del proyecto
Java 21	JDK utilizado para desarrollar y ejecutar la práctica.
Java 17 (release)	Versión de compatibilidad del bytecode configurada para la compilación.
Maven 3.9.9 / Maven Wrapper	Compilación, dependencias y empaquetado de ambos módulos.
OSGi Core 8.0.0	Interfaces y mecanismos del entorno modular OSGi.
Apache Felix Maven Bundle Plugin 5.1.9	Generación de JAR con metadatos de bundle OSGi.
Apache Karaf 4.4.12	Contenedor donde instalamos, iniciamos y detuvimos los bundles.
Visual Studio Code y PowerShell	Edición del código y ejecución de comandos en Windows.
Git y GitHub	Control de versiones, historial de cambios y entrega de evidencias.
7-Zip	Extracción de Karaf después de un problema con las rutas de Windows.
Importante: OSGi es la tecnología que permite organizar y administrar módulos dinámicamente. Karaf es el contenedor que utilizamos para ejecutarlos y administrarlos desde una consola.

3. ¿Qué construimos?
La solución se dividió en dos bundles funcionales.

Bundle A: proveedor de saludos (saludo-api)
Este módulo contiene:

SaludoService: interfaz que define el método saludar(String nombre).
SaludoServiceImpl: implementación que construye el saludo.
SaludoActivator: componente que registra el servicio cuando se inicia el bundle y lo retira cuando se detiene.
El saludo que devuelve el servicio es:

Hola, Samuel! Bienvenido al sistema OSGi.
La interfaz se exporta como un paquete OSGi para que otros bundles puedan conocerla. La implementación permanece interna al proveedor.

Bundle B: consumidor de saludos (saludo-cliente)
Este módulo contiene ClienteActivator. Cuando inicia:

Se registra como oyente de cambios del servicio SaludoService.
Busca el servicio en el registro de OSGi.
Si está disponible, lo utiliza para mostrar el saludo.
Si el servicio desaparece, informa que ya no está disponible, pero el bundle consumidor no se detiene.
Si el servicio vuelve a registrarse, lo detecta y lo utiliza nuevamente.
¿Cómo se comunican?
┌───────────────────────────────────┐
│ Bundle A: saludo-api              │
│                                   │
│ SaludoService (interfaz pública)  │
│ SaludoServiceImpl (implementación)│
│ SaludoActivator                   │
└─────────────────┬─────────────────┘
                  │ registra / retira
                  ▼
         ┌───────────────────┐
         │ Registro de       │
         │ servicios OSGi    │
         └─────────┬─────────┘
                   │ consulta / escucha cambios
                   ▼
┌───────────────────────────────────┐
│ Bundle B: saludo-cliente          │
│                                   │
│ ClienteActivator                  │
│ Consume SaludoService             │
└───────────────────────────────────┘
¿Por qué se eligió este diseño? Porque el consumidor trabaja con la interfaz y con el registro de servicios, no crea directamente una instancia de SaludoServiceImpl. Así puede detectar cuándo un proveedor está o no disponible. Esto reduce el acoplamiento entre implementaciones, aunque el consumidor sigue necesitando el contrato compartido de la interfaz.

4. Organización del repositorio
OSGI/
├── .mvn/                         # Archivos de Maven Wrapper
├── evidencias/
│   ├── 01-bundles-activos.png
│   ├── 02-proveedor-detenido.png
│   ├── 03-proveedor-reiniciado.png
│   └── 04-consumidor-detenido.png
├── saludo-api/
│   ├── pom.xml
│   └── src/main/java/ec/udla/saludo/
│       ├── api/SaludoService.java
│       └── impl/
│           ├── SaludoServiceImpl.java
│           └── SaludoActivator.java
├── saludo-cliente/
│   ├── pom.xml
│   └── src/main/java/ec/udla/saludo/cliente/
│       └── ClienteActivator.java
├── .gitignore
├── EVIDENCIAS.md
├── README.md
├── mvnw
├── mvnw.cmd
└── pom.xml                       # Proyecto Maven principal
El archivo pom.xml principal agrupa los módulos saludo-api y saludo-cliente. Cada módulo tiene su propio pom.xml, por lo que Maven puede compilar y empaquetar los dos dentro de un mismo proyecto.

5. ¿Cómo desarrollamos el proyecto?
Seguimos una secuencia de pasos y registramos cada avance importante con Git:

Primero, preparamos el repositorio. Creamos la estructura inicial, configuramos Git y vinculamos el proyecto con GitHub.

Después, configuramos Maven. Agregamos Maven Wrapper para poder ejecutar la compilación sin exigir una instalación global de Maven. Se configuró la compilación con release 17, aunque en las pruebas utilizamos JDK 21.

Luego, desarrollamos el proveedor. Definimos la interfaz del servicio, su implementación y el activador encargado de registrarlo en OSGi.

A continuación, desarrollamos el consumidor. Este módulo busca SaludoService, invoca el saludo y escucha los eventos de disponibilidad del servicio.

Finalmente, compilamos, instalamos los bundles en Karaf y probamos sus estados. Guardamos capturas y documentamos lo que sucedió en cada prueba.

6. Cómo compilar el proyecto
6.1. Descargar el repositorio
En PowerShell:

git clone https://github.com/CoboSamuel/OSGI.git
cd OSGI
6.2. Comprobar Java
java -version
La implementación fue probada con JDK 21.0.12. Para compilar se requiere un JDK compatible con la configuración del proyecto.

6.3. Compilar e instalar los JAR en el repositorio local de Maven
Desde la carpeta principal OSGI:

.\mvnw.cmd clean install
El comando realiza la compilación y el empaquetado de los dos módulos e instala sus artefactos en el repositorio local de Maven (.m2). En nuestra ejecución obtuvimos:

Sistema OSGi con Apache Karaf ......... SUCCESS
Bundle Proveedor de Saludos ........... SUCCESS
Bundle Consumidor de Saludos .......... SUCCESS
BUILD SUCCESS
Los JAR generados quedan en:

saludo-api/target/saludo-api-1.0.0.jar
saludo-cliente/target/saludo-cliente-1.0.0.jar
También se instalaron en el repositorio local de Maven, lo que permitió invocarlos posteriormente desde Karaf mediante sus coordenadas mvn:.

Alcance de esta comprobación: en la compilación apareció No tests to run, por lo que BUILD SUCCESS demuestra que el proyecto compiló y se empaquetó correctamente; las pruebas funcionales se realizaron manualmente en Karaf, como se explica más adelante.

7. Cómo instalamos e iniciamos Apache Karaf
Utilizamos Apache Karaf 4.4.12 en Windows y lo ubicamos en:

C:\Karaf\apache-karaf-4.4.12
Durante la preparación aparecieron dos problemas que también documentamos porque forman parte del proceso real de instalación.

7.1. Error de Windows: ruta demasiado larga
Al intentar extraer el ZIP con el Explorador de Windows apareció:

Error 0x80010135: Path too long
Para resolverlo mantuvimos el ZIP en una ruta corta (C:\Karaf) y usamos 7-Zip desde PowerShell:

& "C:\Program Files\7-Zip\7z.exe" x "C:\Karaf\apache-karaf-4.4.12.zip" "-oC:\Karaf" -y
La extracción terminó con:

Everything is Ok
Folders: 1228
Files: 701
Verificamos que el ejecutable de Karaf existiera:

Test-Path "C:\Karaf\apache-karaf-4.4.12\bin\karaf.bat"
Resultado: True.

7.2. Error al iniciar: JAVA_HOME sin configurar
El primer intento de iniciar Karaf mostró:

JAVA_HOME not set
Unable to retrieve JAVA_HOME from Registry
JAVA_HOME is not valid: ""
Configuramos Java para esa sesión de PowerShell:

$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.12"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
La ruta es la utilizada en nuestra computadora. En otro equipo debe reemplazarse por la ubicación real del JDK. Esta configuración se aplica a la terminal actual.

Comprobamos que existiera Java en esa ubicación:

Test-Path "$env:JAVA_HOME\bin\java.exe"
Resultado: True.

7.3. Inicio correcto de Karaf
Ejecutamos:

cd C:\Karaf\apache-karaf-4.4.12
.\bin\karaf.bat
Apareció la consola:

Apache Karaf (4.4.12)
karaf@root()>
Dentro de Karaf ejecutamos:

bundle:list
Inicialmente observamos un servicio interno de Karaf activo (ID 33). Con eso verificamos que el contenedor estaba encendido y aceptaba comandos OSGi.

Los comandos bundle:* se ejecutan dentro de Karaf, cuando aparece karaf@root()>. Los comandos git, mvnw y las variables $env: se ejecutan en PowerShell.

8. Instalación y comprobación de los dos bundles
Con Karaf iniciado y los artefactos compilados, instalamos primero el proveedor:

bundle:install -s mvn:ec.udla/saludo-api/1.0.0
Karaf respondió:

[saludo-api] Servicio de saludos registrado.
Bundle ID: 53
Luego instalamos el consumidor:

bundle:install -s mvn:ec.udla/saludo-cliente/1.0.0
La respuesta fue:

[saludo-cliente] Bundle B iniciado.
[saludo-cliente] Hola, Samuel! Bienvenido al sistema OSGi.
Bundle ID: 54
El parámetro -s indica que el bundle debe iniciarse al instalarse.

Después ejecutamos bundle:list y obtuvimos:

ID │ State  │ Lvl │ Version │ Name
33 │ Active │  80 │ 4.4.12  │ Apache Karaf :: OSGi Services ::
53 │ Active │  80 │ 1.0.0   │ Bundle Proveedor de Saludos
54 │ Active │  80 │ 1.0.0   │ Bundle Consumidor de Saludos
¿Qué comprobamos aquí? Que ambos módulos estaban Active y que el consumidor había localizado el servicio del proveedor y ejecutado el método de saludo.

Los dos bundles activos

9. Pruebas de inicio, parada y recuperación
Esta fue la parte principal del trabajo: comprobar el comportamiento de los módulos sin cerrar Karaf.

Nota: los IDs 53 y 54 son los obtenidos en nuestra sesión. Si se instala el proyecto en otra computadora, es necesario consultar los IDs reales con bundle:list.

Prueba 1. Detener el proveedor sin detener el consumidor
Ejecutamos:

bundle:stop 53
bundle:list
La consola mostró:

[saludo-cliente] Servicio no disponible.
[saludo-cliente] El Bundle B sigue activo.
[saludo-api] Servicio de saludos detenido.
Y los estados cambiaron a:

53 │ Resolved │ Bundle Proveedor de Saludos
54 │ Active   │ Bundle Consumidor de Saludos
Resultado: el proveedor se detuvo, pero el consumidor permaneció activo. Como el servicio ya no estaba disponible, el consumidor lo detectó e informó la situación. No pudo continuar generando saludos mientras el proveedor estuvo detenido, pero no se cayó ni fue necesario reiniciarlo.

Proveedor detenido y consumidor activo

Prueba 2. Reiniciar el proveedor y recuperar el servicio
Sin reiniciar Karaf ni el consumidor, ejecutamos:

bundle:start 53
bundle:list
La consola mostró:

[saludo-cliente] Servicio disponible nuevamente.
[saludo-cliente] Hola, Samuel! Bienvenido al sistema OSGi.
[saludo-api] Servicio de saludos registrado.
Ambos bundles volvieron a aparecer Active.

Resultado: el consumidor detectó automáticamente el regreso del servicio y volvió a utilizarlo. Esta prueba demuestra que el registro de servicios OSGi permite recuperar una funcionalidad durante la ejecución, sin reiniciar los demás módulos.

Proveedor reiniciado y comunicación recuperada

Prueba 3. Detener el consumidor sin detener el proveedor
Ejecutamos:

bundle:stop 54
bundle:list
La consola mostró:

[saludo-cliente] Bundle B detenido.
Los estados fueron:

53 │ Active   │ Bundle Proveedor de Saludos
54 │ Resolved │ Bundle Consumidor de Saludos
Resultado: el consumidor se detuvo, mientras el proveedor permaneció activo y conservó su ciclo de vida independiente.

Consumidor detenido y proveedor activo

Prueba 4. Volver a iniciar el consumidor
Ejecutamos:

bundle:start 54
Karaf respondió:

[saludo-cliente] Bundle B iniciado.
[saludo-cliente] Hola, Samuel! Bienvenido al sistema OSGi.
Resultado: el consumidor pudo arrancar nuevamente y recuperar el uso del servicio. No hizo falta reconstruir los JAR ni reiniciar Karaf.

Resumen de resultados
Comprobación	Resultado observado
Compilación de los módulos	BUILD SUCCESS
Instalación del proveedor	Bundle 53 Active
Instalación del consumidor	Bundle 54 Active y saludo mostrado
Detener proveedor	53 Resolved; 54 Active
Volver a iniciar proveedor	Ambos Active; saludo recuperado
Detener consumidor	53 Active; 54 Resolved
Volver a iniciar consumidor	Consumidor iniciado; saludo mostrado
10. ¿Qué conceptos de arquitectura demostramos?
Modularidad. Cada bundle tiene una responsabilidad: uno publica el servicio y el otro lo consume.

Bajo acoplamiento. El consumidor se comunica a través de SaludoService y del registro OSGi, en lugar de instanciar la implementación concreta. La dependencia con la interfaz compartida sí existe y es intencional.

Gestión independiente del ciclo de vida. Karaf permitió ejecutar bundle:stop y bundle:start sobre cada módulo individualmente, sin apagar el contenedor.

Disponibilidad parcial. Al detener el proveedor, el consumidor permaneció activo, aunque el saludo quedó temporalmente fuera de servicio. Esto no significa que toda la funcionalidad siguiera disponible, sino que el módulo consumidor pudo manejar la ausencia de su dependencia.

Recuperación dinámica. Cuando el proveedor volvió a registrarse, el consumidor recibió el evento correspondiente y recuperó la funcionalidad sin reiniciarse.

Mantenibilidad. Separar el contrato, la implementación y el consumo facilita entender y modificar cada responsabilidad.

11. Evidencias incluidas en GitHub
Las capturas de pantalla se encuentran en la carpeta `evidencias/` y están explicadas con más detalle en `EVIDENCIAS.md`.

Archivo	Qué demuestra
01-bundles-activos.png	Ambos bundles activos y comunicación exitosa.
02-proveedor-detenido.png	Proveedor detenido; consumidor todavía activo.
03-proveedor-reiniciado.png	Recuperación del servicio al reiniciar el proveedor.
04-consumidor-detenido.png	Consumidor detenido; proveedor todavía activo.
Estas evidencias corresponden a una ejecución real, no a estados simulados.

12. Historial del desarrollo con Git
El trabajo se dividió en commits para registrar los avances principales:

Commit	Cambio realizado
0f0d92b	Inicialización del proyecto OSGi.
253b9c9	Configuración de Maven y Maven Wrapper.
ab29150	Implementación del bundle proveedor de saludos.
7dba1c1	Implementación del bundle consumidor.
b781f4f	Documentación de las pruebas de ciclo de vida y sus capturas.
El historial puede consultarse en los commits del repositorio.

13. Conclusión
Con este trabajo pasamos de tener dos módulos Java compilados a ejecutarlos realmente dentro de Apache Karaf. Primero verificamos que el proyecto se construyera correctamente; después instalamos ambos bundles, comprobamos que se comunicaban y probamos qué ocurría cuando uno dejaba de estar disponible.

La prueba más importante fue detener el proveedor y observar que el consumidor permanecía activo. Al iniciar nuevamente el proveedor, el consumidor recuperó automáticamente el servicio y volvió a mostrar el saludo. También comprobamos el escenario contrario: detener el consumidor no detuvo el proveedor.

En conclusión, OSGi y Apache Karaf permitieron demostrar modularidad, comunicación mediante servicios y administración independiente de los bundles durante la ejecución. Las capturas, los comandos y los resultados incluidos en este repositorio permiten revisar cómo se comprobó cada requisito.

