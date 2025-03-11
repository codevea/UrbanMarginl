Ce dépôt contient le code de l'application avec, pour chaque commit, une étape de réalisation. <br>
Il contient aussi, dans le wiki, plusieurs guides liés à cette application. <br>

<h1>Présentation générale</h1>

<h2>But de l'application</h2>
L'application est un jeu multi-joueurs en 2D. <br>
Il n'y a qu'une seule application qui permet de démarrer soit un jeu serveur, soit un jeu client :
<ul>
   <li>Le serveur se met à l'écoute des clients (joueurs) qui se connectent au jeu et affiche la plateforme du jeu (l'arène) sans pouvoir agir dessus.</li>
   <li>Le client se connecte au serveur pour entrer dans le jeu : il doit d'abord choisir un personnage pour pouvoir entrer dans l'arène où il pourra se déplacer, tirer des boules de feu vers les autres joueurs, être touché par une boule de feu et discuter dans une zone de tchat.</li>
</ul>

![Capture1](https://github.com/user-attachments/assets/8df0e586-522a-407a-a42d-42e989792b32)

<h2>Caractéristiques techniques</h2>

<h3>Développement de l'application </h3>
<strong>Langage : </strong>Java <br>
<strong>IDE : </strong> Eclipse 2024-06 <br>
<strong>Module graphique dans l'IDE : </strong> WindowBuilder <br>
<strong>Rétroconception : </strong> ObjectAid

<h3>Utilisation de l'application </h3>
<strong>Type de support : </strong>ordinateur de bureau (le jeu n'est pas adapté pour les supports mobiles ni pour les tablettes). <br>
<strong>Réseau : </strong>Internet ou réseau local. <br>
<strong>Connexion : </strong>à partir d'une adresse IPV4. <br>
<strong>Port d'écoute du serveur : </strong>6666 (valeur en dur dans l'application) <br>

<h3>Fichiers des avatars (joueurs)</h3>
Les fichiers contenant les images des avatars sont dans le dossier "media > personnages". <br>
Pour donner une illusion de mouvement, il existe plusieurs images pour le déplacement de chaque avatar (4 étapes de déplacement), pour la blessure lors de la réception d'une boule de feu (2 étapes) et pour la mort (2 étapes). 3 avatars sont fournis avec leurs images dans les 8 positions. <br>
Au niveau des fichiers correspondants, les noms sont constitués de la façon suivante : <br>
persoPENdD.gif
avec :
<ul>
   <li> P contenant le numéro du personnage</li>
   <li> E contenant l'état ("marche", "touche", "mort")</li>
   <li> N contenant le numéro d'étape (de 1 à 4 pour la marche, de 1 à 2 pour touché ou mort)</li>
   <li> D contenant le numéro de direction (0 pour gauche, 1 pour droite)</li>
</ul>
Exemple : "perso1marche1d0.gif" correspond au personnage 1, à l'étape 1 de marche et dirigé vers la gauche (c'est le tout premier personnage en haut à gauche du tableau).

<h3>Fichiers des sons</h3>
Les fichiers contenant les sons du jeu sont dans le dossier "media > sons". <br>
Certaines manipulations provoquent des sons :
<ul>
   <li>Choix du joueur > clic sur le bouton suivant : suivant.wav</li>
   <li>Choix du joueur > clic sur le bouton précédent : precedent.wav</li>
   <li>Choix du joueur > clic sur le bouton go : go.wav</li>
   <li>Entrée dans la fenêtre du choix du joueur : welcome.wav</li>
   <li>Tir d'une boule : fight.wav</li>
   <li>Joueur blessé : hurt.wav</li>
   <li>Joueur mort : death.wav</li>
</ul>


<h1>Présentation du jeu </h1>

<h2>Fenêtre de démarrage du jeu </h2>

![Connexion](https://github.com/user-attachments/assets/38e147d9-8432-4472-965b-a64f6e586b1f)

<br>
Cette première fenêtre permet de choisir d’être serveur ou client. <br>

<h3>Lancer un serveur unique</h3>
En cliquant sur "Démarrer", un jeu serveur est lancé et l'arène du jeu va directement s'afficher. Aucune action ne peut être faite sur le jeu serveur qui permet juste de visualiser le jeu. <br>
Cependant, une fois le serveur lancé, il se met en attente de connexion de clients qui veulent jouer. À chaque fois qu'une connexion est requise, il doit enregistrer ses caractéristiques. <br>
C'est aussi le serveur qui est responsable du transfert de toutes les informations : à chaque fois qu'un joueur (client) réalise une action que les autres joueurs doivent voir (déplacement, tir de boule…), l'information est transmise au serveur qui la renvoie vers tous les joueurs pour qu'elle s'affiche sur leur écran. <br>

<h3>Se connecter à un serveur pour jouer</h3>
En cliquant sur "Connecter", l'ordinateur contrôle d'abord qu'une adresse IP a bien été saisie. Il cherche ensuite à se connecter à cette adresse IP, sur le port défini dans le programme (la valeur du port est une constante). <br>
Si la connexion échoue, un message est affiché. Si la connexion réussie, cette fenêtre se ferme et la fenêtre du choix du personnage apparait.

<h2>Fenêtre de choix du personnage </h2>

![ChoixJoueur](https://github.com/user-attachments/assets/48b897b1-df50-4220-8668-2a6ec13003c3)

<br>
La fenêtre du choix du personnage permet au joueur de saisir son pseudo et de choisir un personnage parmi plusieurs avatars proposés, en utilisant les flèches. <br>
Des zones cliquables transparentes sont positionnées sur les flèches et sur "GO". <br>
Une fois le personnage choisi et le pseudo saisi, il suffit de cliquer sur GO. L'ordinateur contrôle qu'un pseudo a bien été saisi. Si c'est le cas, cette fenêtre se ferme et la fenêtre de l'arène apparait.

<h2>Fenêtre de l’arène </h2>

<h3>Présentation </h3>
La fenêtre de l'arène représente la zone du jeu. Elle contient la zone de jeu avec 20 murs posés aléatoirement (les murs peuvent se superposer) et les joueurs déjà présents(qui ne peuvent pas se superposer entre eux ou avec un mur). Elle contient aussi une zone de saisie (uniquement côté client) et une zone de visualisation pour le tchat.<br>

![Arene](https://github.com/user-attachments/assets/70332d04-85cd-40fa-9e01-8118759c9c90)

<br>
Côté serveur, elle permet juste de visualiser ce qui se passe et ne permet aucune action. <br>
Côté client, elle permet de jouer et de discuter.

<h3>Entrée du joueur dans l'arène </h3>
Le joueur apparaît dans l'arène à une position aléatoire (en évitant d'être sur un mur ou un autre joueur). Il apparait avec son avatar et, en dessous, un message précisant le pseudo suivi de la vie restante (exemple : "Emds : 5").  <br>
En début de jeu, tous les joueurs ont le même nombre de points de vie, défini dans une constante (10 points).

<h3>Zone de discussion (tchat) </h3>
En dessous de l'arène de jeu se trouve une zone de discussion. Le joueur peut cliquer dans la ligne de saisie pour saisir un message et valider. Le message est envoyé au serveur qui l'affiche et le renvoie à tous les joueurs pour qu'il soit affiché dans toutes les zones de discussion. <br>
Pendant qu'il écrit le message et tant qu'il n'a pas validé, les flèches et la barre d'espace ne sont plus actives (il ne peut pas jouer). <br>
Dès que le joueur a validé son message pour l'envoyer, les flèches et la barres d'espace sont à nouveau actives (il peut continuer à jouer).

<h3>Déplacement du joueur et attaque </h3>

<h4>Déplacement : </h4>
Le joueur peut se déplacer (avec les flèches) de haut en bas et de gauche à droite, dans la limite de la taille de l'arène et sans pouvoir passer par-dessus un mur ou un autre joueur.
<h4>Tir : </h4>
Le joueur peut tirer une boule de feu (avec la barre d'espace). La boule avance toujours horizontalement et dans la direction de marche du joueur (vers la gauche ou vers la droite). La boule disparait si elle atteint un obstacle (bord de l'arène, mur, autre joueur).
<h4>Blessure : </h4>
Si la boule touche un joueur, celui-ci perd de la vie et le joueur à l'origine de l'attaque gagne de la vie. Le nombre de points de vie perdu est défini dans une constante (2 points), idem pour le nombre de points de vie gagné (1 point).
<h4>Mort : </h4>
Si la vie d'un joueur arrive à 0, il meurt et ne peut plus rien faire, excepté discuter, mais il reste cependant présent dans l'arène, allongé, sous forme d'obstacle (un autre joueur ou une boule ne peut pas passer sur lui).
<h4>Gestion de la boule : </h4>
Pendant que la boule avance toute seule dans l'arène, le jeu continue (les joueurs peuvent se déplacer, tirer des boules, discuter…). 

<h3>Fermeture d'une fenêtre </h3>
Si un joueur ferme sa fenêtre de jeu, le serveur en est informé et le fait disparaître de l'arène. <br>
Si la fenêtre du serveur est fermée, toutes les fenêtres des clients sont fermées.
