package com.example.demo.domaine;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class Vehicule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;

    @Enumerated(EnumType.STRING)
    private CategorieVehicule categorie;

    private BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    private StatutVehicule statut;

    @ManyToMany
    @ToString.Exclude
    @Builder.Default
    private List<Equipement> equipements = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude
    private Agence agence;

    @OneToMany(mappedBy = "vehicule")
    @ToString.Exclude
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "vehicule")
    @ToString.Exclude
    @Builder.Default
    private List<Maintenance> maintenances = new ArrayList<>();
}