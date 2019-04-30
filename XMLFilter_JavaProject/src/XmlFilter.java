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
	//Folder Path where all XMl files present.
	static String folderPath="D:/JCI project/FilterFilesForJCI";
	//Names which you want to filter from ExcelSheet
	static String includeNames[]= {"valve","Valve","VALVE","Gasket","GASKET","gasket","strainer","Strainer","STRAINER","COUPLING","coupling","Coupling",
			"FLANGE","Flange","flange","FLTR","fltr","Filter","FILTER","filter","pump","PUMP","Pump","TRANS","Trans","trans","coal","Coal","COALESCER","Coalescer","coalescer"};
	//List of exclude names 
	static String excludeNames[]= {"KIT","Kit","kit","MTG","Mtg","mtg","M/Steel","BRKT","MOTOR","PIP","Pip","pip"};

	
	public static void main(String[] args) 
	{

		ArrayList<String> fileNames =getAllFilesFromFolder(folderPath);
		FilterXmlFiles(fileNames);

	}

	private static void FilterXmlFiles(ArrayList<String> fileNames)
	{
		for(String fname:fileNames)
		{
			String currentFile =folderPath+"/"+fname;
			System.out.println(" File Filtering : "+currentFile);
			List <Data> filterData = new LinkedList<Data>();
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
					Cell cell =row.getCell(4);

					String currentCellValue =cell.getStringCellValue(); //Getting Description
					String matnumber = row.getCell(3).getStringCellValue();

					//System.out.println("--------#########################--------");
					flag=true;

					//Checking Duplicate 
					if(setTORemoveDuplicate.contains(matnumber))
					{
						flag=false;
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
								filterData.add(new Data(matnumber,currentCellValue,String.valueOf(row.getCell(6).getNumericCellValue()) ) );
								setTORemoveDuplicate.add(matnumber);
							}
						}
					}
				}
				
				//Closing FileInputStream 
				file.close();
				
				//Sorting filter data A-Z
				Collections.sort(filterData);
			
				//	System.out.println("************Create Sheet ************"); 	
				XSSFSheet filterDataSheet = workbook.createSheet("FilteredData");
				//	System.out.println("************ Writing Data into Sheet ************");

				int rownum = 0; 


				for(Data currData: filterData)
				{					
					String values[]=new String[3];
					values[0] =currData.getMaterialNumber();
					values[1] =currData.getDescription();
					values[2] =currData.getQuantity();	
					Row row = filterDataSheet.createRow(rownum++);
					for(int i=0;i< values.length;i++ )
					{
						Cell cell = row.createCell(i);
						cell.setCellValue(values[i]);						
					}					
				}

				//Writing data into file
				OutputStream	fileOut = new FileOutputStream(currentFile);			
				workbook.write(fileOut);
				System.out.println("************ Data Filter successfully ************"); 

				fileOut.close();

			} catch (FileNotFoundException e) 
			{			
				e.printStackTrace();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}		 		

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
