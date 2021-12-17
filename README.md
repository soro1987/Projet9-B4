# Projet9-B4

Neuvième projet de la formation openclassroom sur les test junit   
Taux de coverage du projet : 86%   
  
Environnement de développement  
Les composants nécessaires lors du développement sont disponibles via des conteneurs docker.  
L'environnement de développement est assemblé grâce à docker-compose.  
Il comporte :  
une base de données PostgreSQL contenant un jeu de données de démo (postgresql://127.0.0.1:9032/db_myerp)  
Lancement  
cd docker/dev  
docker-compose up  
Arrêt  
cd docker/dev  
docker-compose stop  
Remise à zero  
cd docker/dev  
docker-compose stop  
docker-compose rm -v  
docker-compose up    
