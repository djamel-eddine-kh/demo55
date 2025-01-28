package come.example.BonPreparation.service;


import come.example.BonPreparation.dto.BonPreparationDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private List<BonPreparationDto> bonPreparationList;

    // Method to get the initialized BonPreparationDto
    public List<BonPreparationDto> getInvoice() {
        return bonPreparationList;
    }

    @PostConstruct
    private void initializeInvoice() {
        bonPreparationList = new ArrayList<>();

        // Create and populate BonPreparationPieceDto list
        bonPreparationList.add(new BonPreparationDto(
                "Labelle", // libelleDepot
                "Mohammed", // nomChauffeur
                101, // codePiece
                "Margarin", // famillePiece
                "Margarin 250 g", // nomPie
                201, // codeClient
                "Client A", // nomClient
                10.5 // quantite
        ));
        bonPreparationList.add(new BonPreparationDto(
                "Labelle", // libelleDepot
                "Mohammed", // nomChauffeur
                101, // codePiece
                "Margarin", // famillePiece
                "Margarin 250 g", // nomPiece
                202, // codeClient
                "Client B", // nomClient
                20.0 // quantite
        ));

        bonPreparationList.add(new BonPreparationDto(
                "Depot A", // libelleDepot
                "Driver 1", // nomChauffeur
                102, // codePiece
                "Family 1", // famillePiece
                "Piece 2", // nomPiece
                202, // codeClient
                "Client B", // nomClient
                15.0 // quantite
        ));

        bonPreparationList.add(new BonPreparationDto(
                "Depot A", // libelleDepot
                "Driver 1", // nomChauffeur
                103, // codePiece
                "Family 2", // famillePiece
                "Piece 3", // nomPiece
                203, // codeClient
                "Client C", // nomClient
                20.0 // quantite
        ));
    }
    }

