

public class Data implements Comparable<Data>
{
	private String serialNumber;
	private String description;
	private String quantity;
	private String materialNumber;

	public Data(String serialNumber,String materialNumber,String description,String quantity)
	{
		super();
		this.description = description;
		this.quantity = quantity;
		this.materialNumber = materialNumber;
		this.serialNumber = serialNumber;
	}

	@Override
	public String toString() {
		return "Data [serialNumber=" + serialNumber + ", description=" + description + ", quantity=" + quantity
				+ ", materialNumber=" + materialNumber + "]";
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
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	@Override
	public int compareTo(Data data)
	{
		//this > data = 1
		//this > data = -1
		//		/this == data =	0
		int compareValue = this.getSerialNumber().compareToIgnoreCase(data.getSerialNumber() ) ;
		if(compareValue > 0)
			return 1;
		else			
			return -1;
	}




}
