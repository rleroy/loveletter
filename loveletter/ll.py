# -*- coding: utf-8 -*-
import numpy as np
from numpy import *
from random import randint

deck = [16,5,2,2,2,2,1,1,1]
players = ["Camélia (0)","Yassir (1)","Narimane (2)","Maïki (3)"]
cards = ["Carte","Garde","Prêtre","Baron","Servante","Prince","Roi","Comtesse","Princesse"]
hands = [[0,0],[0,0],[0,0],[0,0]]
life = [True,True,True,True,4]
played = [[0],[0],[0],[0],[0]]
active_player = 0

def print_deck ():
	print "La pioche contient"
	for i in range(9):
		print str(deck[i]) + " " + cards[i]

def draw (player):
	depth = np.random.randint(deck[0])
	deck[0]=deck[0]-1
	rank = 1
	while depth >= deck[rank]:
		depth = depth - deck[rank]
		rank=rank+1
	deck[rank]=deck[rank]-1
	if hands[player][0]==0:
		hands[player][0]=rank
	else:
		hands[player][1]=rank
	print players[player] + " pioche " + cards[rank] + " (" + str(rank) + ") ."

def next(player):
	fin = False
	while not fin:
		player = (player + 1) % 4
		if life[player]:
			fin = True
	return player

	

def turn(player):
	print "C'est au tour de " + players[player]
	draw(player)
	print players[player] + " a " + cards[hands[player][0]] + " (a) et " + cards[hands[player][1]] + " (b)"
#	choice = ""
#	while not(choice=="a" or choice=="b"):
#		choice = raw_input("Quelle carte ? ")
	if (hands[player][0] == 7) and (hands[player][1] == 5 or hands[player][1] == 6):
		choice = 0
	elif (hands[player][1] == 7) and (hands[player][0] == 5 or hands[player][0] == 6):
		choice = 1
	else:
		choice = np.random.randint(2)
	if choice==0:
		carte_jouee=hands[player][0]
		print players[player] + " joue " + cards[hands[player][0]]
		hands[player][0] = hands[player][1]
		hands[player][1] = 0
	else:
		carte_jouee=hands[player][1]
		print players[player] + " joue " + cards[hands[player][1]]
		hands[player][1] = 0
	if carte_jouee == 1:
		garde(player)
	elif carte_jouee == 2:
		pretre(player)
	elif carte_jouee == 3:
		baron(player)
	elif carte_jouee==5:
		prince(player)
	elif carte_jouee==6:
		roi(player)
	elif carte_jouee==8:
		princesse(player)
	if life[player]:	
		print players[player] + " a " + cards[hands[player][0]] + " en main."
	

def winner():
	high =  0
	vainqueur = []
	for i in range(4):
		if hands[i] == high:
			vainqueur = vainqueur.append(i)
		elif hands[i] > high:
			high = hands[i]
			vainqueur = [i]
	return vainqueur

def display_winner(vainq):
	if len(vainq) == 1:
		print players[vainq[0]] + " gagne la partie."
	else:
		print "Égalité !"

def garde(joueur):
	played[joueur].append(1)
	cible = -1
	while (cible < 0) or (cible > 3) or not life[cible] or played[cible][len(played[cible])-1] == 4:
		try:
			cible = np.random.randint(4)
			#cible = int(raw_input("Quel joueur ? "))
		except:
			cible = -1
	choix = -1
	while (choix < 2) or (choix > 8):
		try:
			choix = np.random.randint(6)+2
			#choix = int(raw_input("Quelle carte ? "))
		except:
			choix = -1
	print players[joueur] + " pense que " + players[cible] +" a " + cards[choix] + "."
	if hands[cible][0] == choix:
		print "C'est le cas ! " + players[cible] + " perd la partie."
		hands[cible][0]=0
		life[cible] = False
		life[4] = life[4]-1
	else:
		print "Ce n'est pas le cas."

def pretre(joueur):
	played[joueur].append(2)
	cible = -1
	while (cible < 0) or (cible > 3) or not life[cible] or played[cible][len(played[cible])-1] == 4:
		try:
			cible = np.random.randint(4)
			#cible = int(raw_input("Quel joueur ? "))
		except:
			cible = -1
	print players[joueur] + " regarde la main de " + players[cible] + "."

def baron(joueur):
	played[joueur].append(3)
	cible = -1
	while (cible < 0) or (cible > 3) or not life[cible] or played[cible][len(played[cible])-1] == 4:
		try:
			cible = np.random.randint(4)
			#cible = int(raw_input("Quel joueur ? "))
		except:
			cible = -1
	print players[joueur] + " défie " + players[cible] +"."
	if hands[cible][0] > hands[joueur][0]:
		print "Mauvaise idée ! " + players[joueur] + " perd la partie."
		hands[joueur][0]=0
		life[joueur] = False
		life[4] = life[4]-1
	if hands[cible][0] < hands[joueur][0]:
		print "Bonne idée ! " + players[cible] + " perd la partie."
		hands[cible][0]=0
		life[cible] = False
		life[4] = life[4]-1
	else:
		print "C'est une égalité."


def servante(joueur):
		played[joueur].append(4)

def prince(joueur):
	played[joueur].append(5)
	cible = -1
	while (cible < 0) or (cible > 3) or not life[cible] or played[cible][len(played[cible])-1] == 4:
		try:
			cible = np.random.randint(4)
			#cible = int(raw_input("Quel joueur ? "))
		except:
			cible = -1
	print players[joueur] + " choisit " + players[cible] +"."
	if hands[cible][0]==8:
		print players[cible] + " défausse la Princesse et perd la partie."
		hands[cible][0]=0
		life[cible] = False
		life[4] = life[4]-1
	else:
		print players[cible] + " défausse " + cards[hands[cible][0]]+ "."
		hands[cible][0]=0
		draw(cible)

def roi(joueur):
	played[joueur].append(6)
	cible = -1
	while (cible < 0) or (cible > 3) or not life[cible] or played[cible][len(played[cible])-1] == 4:
		try:
			cible = np.random.randint(4)
			#cible = int(raw_input("Quel joueur ? "))
		except:
			cible = -1
	print players[joueur] + " échange sa main avec " + players[cible] +"."
	hands[joueur][0], hands[cible][0] = hands[cible][0], hands[joueur][0]
	print players[cible] + " a " + cards[hands[cible][0]] + " en main."

def comtesse(joueur):
	played[joueur].append(7)

def princesse(joueur):
	played[joueur].append(8)
	print players[joueur] + " perd la partie."
	hands[joueur][0]=0
	life[joueur] = False
	life[4] = life[4]-1

#players[0] = raw_input("Qui est le premier joueur ? ")
#players[1] = raw_input("Qui est le deuxième joueur ? ")
#players[2] = raw_input("Qui est le troisième joueur ? ")
#players[3] = raw_input("Qui est le quatrième joueur ? ")


print "Début de la partie. Les joueurs sont " + players[0] + ', '+ players[1] + ', '+ players[2] + ', et '+ players[3] + '.'

#print_deck()

for i in range(4):
	draw(i)
	
active_player = np.random.randint(4)

print players[active_player] + " commence."

while (deck[0] > 1) and (life[4] > 1):
	turn(active_player)
	active_player=next(active_player)

#tie = False
#for 

display_winner(winner())
