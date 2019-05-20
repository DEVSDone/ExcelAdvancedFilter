import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class XmlFilter 
{
	//Folder Path where all Excel files present.
	static String folderPath="D:/JCI project/FilterFilesForJCI";
	//Names which you want to filter from ExcelSheet
	static String includeNames[]= {"valve","Valve","VALVE","Gasket","GASKET","gasket","strainer","Strainer","STRAINER","COUPLING","coupling","Coupling",
			"FLANGE","Flange","flange","FLTR","fltr","Filter","FILTER","filter","pump","PUMP","Pump","TRANS","Trans","trans","coal","Coal","COALESCER","Coalescer","coalescer"};
	//List of exclude names 
	static String excludeNames[]= {"KIT","Kit","kit","MTG","Mtg","mtg","M/Steel","BRKT","MOTOR","PIP","Pip","pip"};

	static List <Data> filterData = new LinkedList<Data>();
	
	public static void main(String[] args) 
	{
		ArrayList<String> fileNames =getAllFilesFromFolder(folderPath);
		FilterXmlFiles(fileNames);
	}

	private static void FilterXmlFiles(ArrayList<String> fileNames)
	{
		int proFiles =1;
		for(String fname:fileNames)
		{
			String serialNum[] = fname.split("-");
			String srNum = serialNum[0];
			
			String currentFile =folderPath+"/"+fname;
			System.out.println(" File Filtering : "+proFiles+" :: "+currentFile);

			//HashSet<Data> filterData =new HashSet<Data>();
			HashSet <String> setTORemoveDuplicate =new HashSet<String>();
			FileInputStream file;
			try
			{
				//To read data from excelsheet
				file = new FileInputStream(new File(currentFile)); 				
				//Create Workbook instance holding reference to .xlsx file
				@SuppressWarnings("resource")
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				//Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);
				//Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				boolean flag;
				while (rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					//For each row, iterate through Particular Column

					String currentCellValue =row.getCell(5).getStringCellValue(); //Getting Description
					String matnumber = row.getCell(4).getStringCellValue();

					//System.out.println("--------#########################--------");
					flag=true;

					//Checking Duplicate 
					if(setTORemoveDuplicate.contains(matnumber))
					{
						flag=false;    //for not add duplicate 
					}

					//Checking data which wnat to exclude
					for(int j=0;j<excludeNames.length;j++)
					{
						if(currentCellValue.contains(excludeNames[j]) )
						{
							//	System.out.println("##currentCellValue.contains(excludeNames[j]): TRUE So BREAK & FLASE"); 
							flag =false;
						}
					}

					if(flag)
					{
						//	System.out.println("##currentCellValue NOT Duplicate nor from exclude list");
						for(int i=0;i<includeNames.length;i++)
						{	
							if( currentCellValue.contains(includeNames[i]) && (!setTORemoveDuplicate.contains(matnumber)) )
							{								
								filterData.add(new Data(srNum,matnumber,currentCellValue,String.valueOf(row.getCell(8).getNumericCellValue()) ) );
								setTORemoveDuplicate.add(matnumber);
							}
						}
					}
				}
				
				//Closing FileInputStream 
				file.close();
				
						} catch (FileNotFoundException e) 
			{			
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}		 		
			proFiles++;
		}
		
		try 
		{
		//Sorting filter data A-Z
		Collections.sort(filterData);
		System.out.println("TOtal Rows :"+filterData.size());
		 // Blank workbook 
		 @SuppressWarnings("resource")
		XSSFWorkbook workbook1 = new XSSFWorkbook(); 
		//	System.out.println("************Create Sheet ************"); 	
		XSSFSheet filterDataSheet = workbook1.createSheet("FilteredAndFormatted_Data");
		//	System.out.println("************ Writing Data into Sheet ************");
		
		int rownum = 0; 
		
		for(Data currData: filterData)
		{					
			String values[]=new String[4];
			values[0] =currData.getSerialNumber();
			values[1] =currData.getMaterialNumber();
			values[2] =currData.getDescription();
			values[3] =currData.getQuantity();	
			Row row = filterDataSheet.createRow(rownum++);
			for(int i=0;i< values.length;i++ )
			{
				Cell cell = row.createCell(i);
				cell.setCellValue(values[i]);						
			}					
		}
		
		//Sizing column 	
		filterDataSheet.autoSizeColumn(0);
		filterDataSheet.autoSizeColumn(1);
		filterDataSheet.autoSizeColumn(2);
		filterDataSheet.autoSizeColumn(3);
		
		//Writing data into file
		OutputStream	fileOut = new FileOutputStream(new File(folderPath+"/FilterAndFormattedExcel.xlsx"));			
		workbook1.write(fileOut);
		System.out.println("************ Data Filter and Formatted successfully ************"); 
						
		fileOut.close();

		}catch(Exception e)
		{
			e.printStackTrace();
		}


	}

	private static ArrayList<String> getAllFilesFromFolder(String directoryName ) 
	{
		ArrayList<String> files =new ArrayList<String>();
		File directory = new File(directoryName);
		//get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList){
			if (file.isFile()){
				//	System.out.println(file.getName());
				files.add(file.getName());
			}
		}			

		return files;
	}



}
