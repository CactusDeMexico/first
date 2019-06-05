## Créez un site communautaire autour de l’escalade

Ceci est le dépôt associé au projet [_Créez un site communautaire autour de l’escalade_](https://openclassrooms.com/projects/creez-un-site-communautaire-autour-de-lescalade)
sur [_OpenClassrooms_](https://www.openclassrooms.com).


## MPD
![](script/mpd.png?raw=true)


### Technologies

- JDK8 version 152
- Apache Tomcat 9.0.16
- Apache Maven 4.0
- PostgreSQL 9.6

### Framework
- Spring MVC
- Spring Security 
- Thymeleaf


### Modules

- `climbing-webapp` : module contenant les vues et ses contrôleurs
- `climbing-business` : module contenant la logique métier
- `climbing-consumer` : module contenant la persistance et le pattern DAO
- `climbing-model` : module contenant les entités de la solution
- `script` : tous les scripts de création de la base de données PostgreSQL




### Déploiement

- Importation du projet dans IntelliJ
- Configuration de la base de données :

Acceder au fichier `climb/src/main/java/resources/application.properties` et trouver les lignes suivantes :
```
url="jdbc:postgresql://localhost:5432/app_climbing"
driverClassName="org.postgresql.Driver"
username="admin_climbing"
password="Shangri_La"
defaultAutoCommit="true"
defaultTransactionIsolation="READ_COMMITTED"
```

Il suffit de paramètrer une base de données avec la même configuration, ou d'adapter cette dernière à une déjà existante.

- Configurer le serveur Apache Tomcat en local :

Dans l'onglet `Deployment` de la configuration du serveur, déployer le war de l'application web ainsi que le dossier présent sur le serveur pour les transferts des fichiers.
Définir une `Application context` pour ce dossier, comme par exemple `/image`, un fichier sera ensuite accessible via cette URL exemple :
`http://localhost:8080/image/user/user-1.jpg` selon l'architecture de votre dossier.