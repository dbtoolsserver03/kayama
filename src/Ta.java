import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Ta {

	public static void main(String[] args) {
		Ta o = new Ta();
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		Calendar c = Calendar.getInstance();
		
		System.out.println(formatter.format(o.getCalendarByType(c,0)));
		System.out.println(formatter.format(o.getCalendarByType(c,1)));
		System.out.println(formatter.format(o.getCalendarByType(c,2)));
		System.out.println(formatter.format(o.getCalendarByType(c,3)));
		System.out.println(formatter.format(o.getCalendarByType(c,4)));
		System.out.println(formatter.format(o.getCalendarByType(c,5)));
		System.out.println(formatter.format(o.getCalendarByType(c,6)));
		System.out.println(formatter.format(o.getCalendarByType(c,7)));
	}
	
	private  Date getCalendarByType(Calendar calendarTargetMonth, int type) {
		calendarTargetMonth.set(2014,01,01);
		switch (type) {
		case 0:
	        //指定月前月末日開始年日時（from）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.set(Calendar.DAY_OF_MONTH, 0);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 0);//時
			calendarTargetMonth.set(Calendar.MINUTE, 0);//分
			calendarTargetMonth.set(Calendar.SECOND, 0);//秒

			break;

		case 1:
	        // 指定月前月末日終了日終了年日時（to）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.set(Calendar.DAY_OF_MONTH, 0);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 23);//時
			calendarTargetMonth.set(Calendar.MINUTE, 59);//分
			calendarTargetMonth.set(Calendar.SECOND, 59);//秒


			break;
		case 2:
	        // 指定月の月初開始日開始年日時（from）
			calendarTargetMonth.add(Calendar.MONTH,-1);
			calendarTargetMonth.set(Calendar.DATE, 1);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 0);//時
			calendarTargetMonth.set(Calendar.MINUTE, 0);//分
			calendarTargetMonth.set(Calendar.SECOND, 0);//秒
			break;


		case 3:
	        // 指定月の月初開始日終了年日時（to）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.set(Calendar.DATE, 1);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 23);//時
			calendarTargetMonth.set(Calendar.MINUTE, 59);//分
			calendarTargetMonth.set(Calendar.SECOND, 59);//秒
			break;

		case 4:
	        // 指定月の月末の終了日開始年日時（from）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.set(Calendar.DAY_OF_MONTH, calendarTargetMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 0);//時
			calendarTargetMonth.set(Calendar.MINUTE, 0);//分
			calendarTargetMonth.set(Calendar.SECOND, 0);//秒
			break;
		case 5:
	        // 指定月の月末の終了日終了年日時（to）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.set(Calendar.DAY_OF_MONTH, calendarTargetMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 23);//時
			calendarTargetMonth.set(Calendar.MINUTE, 59);//分
			calendarTargetMonth.set(Calendar.SECOND, 59);//秒
			break;
		case 6:
	        //前日開始年日時（from）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.add(Calendar.DATE,-1);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 0);//時
			calendarTargetMonth.set(Calendar.MINUTE, 0);//分
			calendarTargetMonth.set(Calendar.SECOND, 0);//秒
			break;
		case 7:
	        //前日終了年日時（to）
			calendarTargetMonth.add(Calendar.MONTH, -1);
			calendarTargetMonth.add(Calendar.DATE,-1);
			calendarTargetMonth.set(Calendar.HOUR_OF_DAY, 23);//時
			calendarTargetMonth.set(Calendar.MINUTE, 59);//分
			calendarTargetMonth.set(Calendar.SECOND, 59);//秒
			break;

		default:
			break;
		}

		return calendarTargetMonth.getTime();
	}
}
