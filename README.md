
# CP_Airplane

Rendu du Projet de Programmation par contraintes dans le cadre de l'UE HAI916I


## recherche des solutions

Lors du lancement de l’application, veuillez éditer les options de lancement comme indiqué ci-dessous:

```bash
java -jar LightningAirlines.jar [-bi|-br] [-a] [-t <timeout in ms>]  [-i <aircraft instance>] [-h] [-c]
```
### arguments

| Option | Description                |
| :-------- | :------------------------- |
| `-bi,--basic_iterative` | Chercher la première solution via un algorithme itératif. |
| `-br,--basic_recursive` | Chercher la première solution via un algorithme récursif. |

Attention : L'instance 8 prend un temps important pour terminer.


| Option | Description                |
| :-------- | :------------------------- |
| `-a,--all` | Chercher toutes les solutions. |

Permet de lister toutes les solutions qui satisfont le problème et non pas la première trouvée. **(incompatible avec '-bi' et '-br')** 


| Option | Description                |
| :-------- | :------------------------- |
| `-h,--help ` |Afficher ce message d’aide |
                       		


| Obligatoire | Description                |
| :-------- | :------------------------- |
| `-i,--instance <aircraft instance>` | Instance du Problème à résoudre (de inst0 à inst10) (**Obligatoire**)|
    	


| Option | Description                |
| :-------- | :------------------------- |
| `-t,--timeout <timeout en ms>` |Définir le temps limite d'attente avant arrêt de la recherche |


| Option | Description                                                                                |
| :-------- |:-------------------------------------------------------------------------------------------|
| `-c,--compare `| Rechercher les solutions via différentes straégies et en comparer les résultats **(incompatible avec '-bi' et '-br')**    |

     	


