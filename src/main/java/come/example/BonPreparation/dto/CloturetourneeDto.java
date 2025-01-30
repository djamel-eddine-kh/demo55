package come.example.BonPreparation.dto;

public class CloturetourneeDto {

		private String produit;
	    private Double stockDepart;
	    private Double chargement;
	    private Double vente;
	    private Double livraison;
	    private Double retour;
	    private Double retourStock;
	    private Double stockFinJournee;
	    private Double ecartStock;
	  
	    
	
		public CloturetourneeDto(String produit, Double stockDepart, Double chargement, Double vente, Double livraison,
				Double retour, Double retourStock, Double stockFinJournee, Double ecartStock) {
		 
			this.produit = produit;
			this.stockDepart = stockDepart;
			this.chargement = chargement;
			this.vente = vente;
			this.livraison = livraison;
			this.retour = retour;
			this.retourStock = retourStock;
			this.stockFinJournee = stockFinJournee;
			this.ecartStock = ecartStock;
		}
		public String getProduit() {
		return produit;
	    }
	    public void setProduit(String produit) {
		this.produit = produit;
	    }
     	public Double getStockFinJournee() {
		return stockFinJournee;
	    }
	    public void setStockFinJournee(Double stockFinJournee) {
		this.stockFinJournee = stockFinJournee;
	    }
		public Double getStockDepart() {
			return stockDepart;
		}
		public void setStockDepart(Double stockDepart) {
			this.stockDepart = stockDepart;
		}
		public Double getChargement() {
			return chargement;
		}
		public void setChargement(Double chargement) {
			this.chargement = chargement;
		}
		public Double getVente() {
			return vente;
		}
		public void setVente(Double vente) {
			this.vente = vente;
		}
		public Double getLivraison() {
			return livraison;
		}
		public void setLivraison(Double livraison) {
			this.livraison = livraison;
		}
	
		public Double getRetour() {
			return retour;
		}
		public void setRetour(Double retour) {
			this.retour = retour;
		}
		public Double getRetourStock() {
			return retourStock;
		}
		public void setRetourStock(Double retourStock) {
			this.retourStock = retourStock;
		}
		public Double getEcartStock() {
			return ecartStock;
		}
		public void setEcartStock(Double ecartStock) {
			this.ecartStock = ecartStock;
		}
		}
