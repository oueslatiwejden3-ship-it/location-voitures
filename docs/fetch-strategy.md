# Stratégie de fetch et de cascade — AutoLoc

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition). Supprimer le contrat supprime ses paiements, et retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence. |
| Agence → Employe | LAZY | Aucune | Un employé n'est pas détruit avec l'agence. |
| Vehicule ↔ Equipement | LAZY | Aucune | Les équipements sont partagés entre véhicules. Utilisation d'un Set pour éviter les doublons. |
| Client → Reservation | LAZY | PERSIST | Enregistrer un nouveau client avec ses réservations les enregistre aussi. L'historique n'est pas supprimé avec le client. |
| Reservation → Vehicule | LAZY | Aucune | Le véhicule existe indépendamment de la réservation. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Le contrat dépend de sa réservation. La clé étrangère est portée par Contrat (id_reservation). |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance enregistrée avec son véhicule est sauvegardée en même temps. |