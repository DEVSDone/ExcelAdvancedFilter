

public class Data implements Comparable<Data>
{
	private String description;
	private String quantity;
	private String materialNumber;

	public Data(String materialNumber,String description,String quantity)
	{
		super();
		this.description = description;
		this.quantity = quantity;
		this.materialNumber = materialNumber;
	}

	@Override
	public String toString() {
		return "Data [description=" + description + ", quantity=" + quantity + ", materialNumber=" + materialNumber
				+ "]";
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getMaterialNumber() {
		return materialNumber;
	}
	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	@Override
	public int compareTo(Data data)
	{
		//this > data = 1
		//this > data = -1
		//		/this == data =	0
		int compareValue = this.getDescription().compareToIgnoreCase(data.getDescription() ) ;
		if(compareValue > 0)
			return 1;
		else			
			return -1;
	}




}
