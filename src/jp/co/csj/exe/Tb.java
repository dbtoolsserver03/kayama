package jp.co.csj.exe;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tb {

	public static void main(String[] args) {
		Tb o = new Tb();
		List<String> lst = new ArrayList<>();
		for(int i=0; i < 11;i++) {
			lst.add(String.format("%03d", i));
		}
		List<List> lstlst =new ArrayList<>();
		List<String> noDone = new ArrayList<>();
		o.getLstLst(lst,20, lstlst, noDone);
		
		System.out.println(lstlst);
		System.out.println(noDone);
	
	}
	public void getLstLst(List lst, int size , List<List> lstlst, List noDone) {
		if (lst.size() < size) {
			for (int i = 0; i < lst.size(); i++) {
				noDone.add(lst.get(i));
			}
		} else if (lst.size() > size) {
			for (int i = 0; i < lst.size();i+=size) {
				List tLst = new ArrayList<>();
				for (int j = 0; j < size&& i+j < lst.size(); j++) {
					tLst.add(lst.get(i+j));
				}
				lstlst.add(tLst);
			}
			
			List endLst = lstlst.get(lstlst.size()-1);
			if (endLst.size()<size) {
				for (int i = 0; i < endLst.size(); i++) {
					noDone.add(endLst.get(i));
				}
				lstlst.remove(lstlst.size()-1);
			}
		} else {
			
			for (int i = 0; i < lst.size();) {
				List tLst = new ArrayList<>();
				for (int j = 0; j < size; j++) {
					tLst.add(lst.get(j));
				}
				lstlst.add(tLst);
			}
		}
	}
}
