# Construction trame

longeurDonnées # données longeurDonnées # données

Nous avons combiné le caractère # pour savoir la fin des chiffres constituant le nombre
Nous utilisons ce nombre pour lire les x bytes de données qui suivent le #

## Exemple
5#LOGIN2#ok19#Tuple bien insere !
	-> 5 = longeur de 'LOGIN'
	-> 2 = longeur de 'ok'
	-> 19 = longeur de 'Tuple bien insere !'


						SENS				Champ1					Champ2				Champ3				Champ4

LOGIN 					C -> S 				LOGIN 					login 				password

LOGOUT 					C -> S 				LOGOUT

GET_AUTHORS 			C -> S 				GET_AUTHORS

GET_SUBJECTS 			C -> S 				GET_SUBJECTS

ADD_AUTHOR 				C -> S 				ADD_AUTHOR 				last_name			first_name 			birth_date

ADD_SUBJECT 			C -> S 				ADD_SUBJECT 			last_name			name

ADD_BOOK 				C -> S 				ADD_BOOK 				last_name			...					...				...