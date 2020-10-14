package jp.co.csj.tools.utils.db;

import jp.co.csj.tools.utils.log.CsjLog4j;
import jp.co.csj.tools.utils.str.CsjStrUtils;

public class DbUtils {

	public static String getSybaseTablesInfoSql(String tblNm) throws Throwable {
		CsjLog4j.logger.debug("AutoDbToXls.getSybaseTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {

			String tableNm = CsjStrUtils.fromAtoBByTrim(tblNm, "(", ")");
			String[] tableNms = tableNm.split(",");

			sb.append(" SELECT ");
			sb.append("     t.TBL_NM_EN, ");
			sb.append("     t.COL_ID, ");
			// sb.append(" t.COL_NM_JP, ");
			sb.append("     t.COL_NM_EN, ");
			sb.append("     t.COL_DEFAULT, ");
			sb.append("     t.COL_TYPE, ");
			sb.append("     t.COL_TYPE_INFO, ");

			sb.append("     t.LENGTH, ");
			sb.append("     t.PRECISION, ");
			sb.append("     t.SCALE, ");
			sb.append("     (case when t.COL_NM_EN = keyinfo.keyname then 'Y' else 'N' end) AS COL_IS_PK, ");
			sb.append("     t.COL_CAN_NULL ");
			sb.append(" FROM ");
			sb.append("     ( ");
			sb.append("         SELECT ");
			sb.append("             o.name AS TBL_NM_EN, ");
			sb.append("             c.colid AS COL_ID, ");
			sb.append("             '' AS COL_NM_JP, ");
			sb.append("             c.name AS COL_NM_EN, ");
			sb.append("             cmt.text AS COL_DEFAULT, ");
			sb.append("             t.name AS COL_TYPE, ");

			sb.append("     (case ");
			sb.append(
					"     WHEN c.prec is not null and c.scale is not null THEN t.name||'('||convert(varchar, c.prec)||','||convert(varchar, c.scale)||')' ");
			sb.append(
					"     WHEN c.prec is not null and c.scale is null THEN  t.name||'('||convert(varchar, c.prec)||')' ");
			sb.append("     WHEN t.name like '%time%' or t.name like '%date%' THEN  t.name ");
			sb.append("     WHEN c.length is not null THEN  t.name||'('||convert(varchar, c.length)||')' ");
			sb.append("     else t.name  ");
			sb.append("      end) AS COL_TYPE_INFO,  ");

			sb.append("             c.prec AS PRECISION, ");
			sb.append("             c.scale AS SCALE, ");
			sb.append("             c.length AS LENGTH, ");
			sb.append("             (case when c.status =8 then 'Y' else 'N' end) AS COL_CAN_NULL ");
			sb.append("         FROM ");
			sb.append("             dbo.sysobjects o, ");
			sb.append("             dbo.syscolumns c ");
			sb.append("         LEFT OUTER JOIN ");
			sb.append("             dbo.syscomments cmt ");
			sb.append("         ON ");
			sb.append("                 c.cdefault = cmt.id, ");
			sb.append("             dbo.systypes t ");
			sb.append("         WHERE ");
			sb.append("                 o.id = c.id ");
			sb.append("             AND o.type = 'U' ");
			sb.append("             AND c.usertype = t.usertype ");
			sb.append("             AND o.name in ");
			sb.append(tblNm);
			sb.append(" ");
			sb.append("     ) t ");
			sb.append(" LEFT OUTER JOIN ");
			sb.append("     ( ");

			sb.append("         SELECT * FROM (");
			for (int i = 0; i < tableNms.length; i++) {
				String key = tableNms[i];

				for (int j = 1; j <= 2; j++) {
					sb.append("         SELECT ");
					sb.append("             " + key + "" + " AS tblNm, ");
					sb.append("             index_col(" + key + ",1," + j + ") AS keyname, ");
					sb.append("             " + j + " AS keypos ");
					if (j < 2) {
						sb.append("         UNION ALL ");
					}
				}
				if (i + 1 < tableNms.length) {
					sb.append("         UNION ALL ");
				}
			}

			sb.append("    ) aa WHERE aa.keyname IS NOT NULL ) keyinfo ON t.COL_NM_EN = keyinfo.keyname ");
			sb.append("     ORDER BY t.TBL_NM_EN,t.COL_ID ");
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}

		CsjLog4j.logger.debug("AutoDbToXls.getSybaseTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	public static String getOracleTablesInfoSql(Object... objs) throws Throwable {
		CsjLog4j.logger.debug("AutoDbToXls.getOracleTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(" SELECT T_CMT.TABLE_TYPE TABLE_TYPE_INFO,    ");
			sb.append("   T_CMT.COMMENTS TBL_NM_JP, ");
			sb.append("   T_CMT.TABLE_NAME TBL_NM_EN,   ");
			sb.append("   T_COL.COLUMN_ID COL_ID,   ");
			sb.append("   T_COL_CMT.COMMENTS COL_NM_JP, ");
			sb.append("   T_COL.COLUMN_NAME COL_NM_EN,  ");
			sb.append("   T_COL.DATA_TYPE COL_TYPE, ");
			sb.append("   DATA_TYPE ");
			sb.append(
					"   ||DECODE(T_COL.DATA_TYPE,'DATE','','CLOB','','BLOB','','BFILE','','FLOAT','','LONG RAW','','LONG','','RAW','('    ");
			sb.append("   || TO_CHAR(T_COL.DATA_LENGTH) ");
			sb.append("   || ')', (DECODE(SIGN(INSTR(T_COL.DATA_TYPE, 'CHAR')),1, '('   ");
			sb.append("   || TO_CHAR(T_COL.DATA_LENGTH) ");
			sb.append(
					"   || ')',(DECODE(SUBSTR(T_COL.DATA_TYPE, 1, 9), 'TIMESTAMP', '', (DECODE(NVL(T_COL.DATA_PRECISION, -1), -1, '',(DECODE(NVL(T_COL.DATA_SCALE, 0), 0, '(' ");
			sb.append("   || TO_CHAR(T_COL.DATA_PRECISION)  ");
			sb.append("   || ')', '('   ");
			sb.append("   || TO_CHAR(T_COL.DATA_PRECISION)  ");
			sb.append("   || ','    ");
			sb.append("   || TO_CHAR(T_COL.DATA_SCALE)  ");
			sb.append("   || ')'))))))))) COL_TYPE_INFO,    ");
			sb.append("   CASE  ");
			sb.append("     WHEN T_PK.COLUMN_NAME IS NULL   ");
			sb.append("     THEN 'N'    ");
			sb.append("     ELSE 'Y'    ");
			sb.append("   END COL_IS_PK,    ");
			sb.append("   T_COL.NULLABLE COL_CAN_NULL,  ");
			sb.append("   T_COL.DATA_DEFAULT COL_DEFAULT    ");
			sb.append(" FROM USER_TAB_COLUMNS T_COL,    ");
			sb.append("   USER_COL_COMMENTS T_COL_CMT,  ");
			sb.append("   USER_TAB_COMMENTS T_CMT,  ");
			sb.append("   (SELECT UC.TABLE_NAME,    ");
			sb.append("     UCC.COLUMN_NAME ");
			sb.append("   FROM USER_CONSTRAINTS UC, ");
			sb.append("     USER_CONS_COLUMNS UCC   ");
			sb.append("   WHERE UC.CONSTRAINT_NAME = UCC.CONSTRAINT_NAME    ");
			sb.append("   AND UC.CONSTRAINT_TYPE   = 'P'    ");
			sb.append("   ) T_PK    ");
			sb.append(" WHERE T_COL.TABLE_NAME =T_COL_CMT.TABLE_NAME    ");
			sb.append(" AND T_COL.TABLE_NAME   =T_CMT.TABLE_NAME    ");
			sb.append(" AND T_COL.TABLE_NAME   =T_PK.TABLE_NAME(+)  ");
			sb.append(" AND T_COL.COLUMN_NAME  =T_PK.COLUMN_NAME(+) ");
			sb.append(" AND T_COL.COLUMN_NAME  =T_COL_CMT.COLUMN_NAME   ");
			if (objs.length > 0) {
				String[] tbls = CsjStrUtils.fromAtoBByTrim(objs[0].toString(), "(", ")").split(",");
				if (tbls.length == 1 && CsjStrUtils.isEmpty(tbls[0])) {

				} else {
					sb.append(" AND (");
					for (int i = 0; i < tbls.length; i++) {

						sb.append("   T_COL.TABLE_NAME = " + tbls[i] + "");
						if (i + 1 != tbls.length) {
							sb.append(" OR ");
						}
					}
					sb.append(")");
				}
			}

			sb.append(" ORDER BY T_COL.COLUMN_ID   ");
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}

		CsjLog4j.logger.debug("AutoDbToXls.getOracleTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	public static String getSqlLiteTablesInfoSql(String tblNm) throws Throwable {
		CsjLog4j.logger.debug("AutoDbToXls.getSqlLiteTablesInfoSql(Object... objs) begin");
		StringBuffer sb = new StringBuffer();
		try {
			// sb.append(" select * from sqlite_master where upper(type)=\"TABLE\" ");

			sb.append(" PRAGMA table_info (" + tblNm + ")   ");

			// if (objs.length > 0) {
			// String[] tbls = CsjStrUtils.fromAtoBByTrim(objs[0].toString(), "(",
			// ")").split(",");
			// if ( tbls.length == 1 && CsjStrUtils.isEmpty(tbls[0])) {
			//
			// } else {
			// sb.append(" AND (");
			// for (int i = 0; i < tbls.length; i++) {
			//
			// sb.append(" name = " + tbls[i]+"");
			// if (i+1!=tbls.length) {
			// sb.append(" OR ");
			// }
			// }
			// sb.append(")");
			// }
			// }

			// sb.append(" ORDER BY T_COL.COLUMN_ID ");
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}

		CsjLog4j.logger.debug("AutoDbToXls.getSqlLiteTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	public static String getSqlMicroTablesInfoSql(Object... objs) throws Throwable {
		StringBuffer sb = new StringBuffer();
		CsjLog4j.logger.debug("AutoDbToXls.getSqlMicroTablesInfoSql(Object... objs) begin");
		try {
			sb.append(" SELECT  ");

			// sb.append(" case ");
			// sb.append(" when a.colorder = 1 then case when f.value is null then '' else
			// f.value end ");
			//
			// sb.append(" else ");
			// sb.append(" '' ");
			// sb.append(" end as TBL_NM_JP ");
			sb.append("       CAST(isnull(g.value,'') as varchar(1000))  as COL_NM_JP,    ");

			sb.append("        a.colorder as COL_ID,    ");
			sb.append("        a.name as COL_NM_EN, ");
			// sb.append(" case ");
			// sb.append(" when COLUMNPROPERTY(a.id, a.name, 'IsIdentity') = 1 then ");
			// sb.append(" 'Y' ");
			// sb.append(" else ");
			// sb.append(" '' ");
			// sb.append(" end as 标识, ");

			// sb.append(" case when a.colorder = 1 then ");
			// sb.append(" d.name ");
			// sb.append(" else ");
			// sb.append(" '' ");
			sb.append("        d.name as TBL_NM_EN ,    ");
			sb.append("        case ");
			sb.append("          when exists    ");
			sb.append("           (SELECT 1 ");
			sb.append("                  FROM sysobjects    ");
			sb.append("                 where xtype = 'PK'  ");
			sb.append("                   and parent_obj = a.id ");
			sb.append("                   and name in   ");
			sb.append("                       (SELECT name  ");
			sb.append("                          FROM sysindexes    ");
			sb.append("                         WHERE indid in (SELECT indid    ");
			sb.append("                                           FROM sysindexkeys ");
			sb.append("                                          WHERE id = a.id    ");
			sb.append("                                            AND colid = a.colid))) then  ");
			sb.append("           'Y'   ");
			sb.append("          else   ");
			sb.append("           'N'   ");
			sb.append("        end as COL_IS_PK,    ");
			sb.append("        (b.name + '(') + ");
			sb.append(
					"        CAST(COLUMNPROPERTY(a.id, a.name, 'PRECISION') as varchar(50)) +  case CAST(isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), '0') as varchar(50)) when '0' then '' else ','+ CAST(isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), '0') as varchar(50)) end + ')'  as COL_TYPE_INFO,   ");
			sb.append("        b.name as COL_TYPE,    ");
			sb.append("        a.length as 占用字节数,   ");
			sb.append("        COLUMNPROPERTY(a.id, a.name, 'PRECISION') as COL_LENGTH,   ");
			sb.append("        isnull(COLUMNPROPERTY(a.id, a.name, 'Scale'), 0) as AFTER_DOT_NUM,    ");
			sb.append("        case ");
			sb.append("          when a.isnullable = 1 then ");
			sb.append("           'Y'   ");
			sb.append("          else   ");
			sb.append("           'N'   ");
			sb.append("        end as COL_CAN_NULL, ");
			sb.append("        isnull(e.text, '') as COL_DEFAULT   ");
			// sb.append(" isnull(g.value, '') as COL_NM_JP ");
			sb.append("   FROM syscolumns a ");
			sb.append("   left join systypes b  ");
			sb.append("     on a.xusertype = b.xusertype    ");
			sb.append("  inner join sysobjects d    ");
			sb.append("     on a.id = d.id  ");
			sb.append("    and d.xtype = 'U'    ");
			sb.append("    and d.name <> 'dtproperties' ");
			sb.append("   left join syscomments e   ");
			sb.append("     on a.cdefault = e.id    ");
			sb.append("   left join sys.extended_properties g   ");
			sb.append("     on a.id = g.major_id    ");
			sb.append("    and a.colid = g.minor_id ");
			sb.append("   left join sys.extended_properties f   ");
			sb.append("     on d.id = f.major_id    ");
			sb.append("    and f.minor_id = 0   ");
			sb.append("  where 1=1     ");

			if (objs.length > 0 && CsjStrUtils.isNotEmpty((String) objs[0])) {
				sb.append(" and d.name in " + objs[0]);
			}
			sb.append("  order by a.id, a.colorder  ");
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}
		CsjLog4j.logger.debug("AutoDbToXls.getSqlMicroTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	public static String getPostgreTablesInfoSql(Object... objs) throws Throwable {
		StringBuffer sb = new StringBuffer();
		CsjLog4j.logger.debug("AutoDbToXls.getPostgreTablesInfoSql(Object... objs) begin");
		try {
			sb.append(" select f.oid, ");
			sb.append("        f.relname as \"TBL_NM_EN\", ");
			sb.append("        f.col_nm_jp as \"COL_NM_JP\", ");
			sb.append("        f.col_type_info as \"COL_TYPE_INFO\", ");
			sb.append("        f.col_nm_en as \"COL_NM_EN\", ");
			sb.append("        (case f.col_can_null ");
			sb.append("          when 't' then ");
			sb.append("           'N' ");
			sb.append("          else ");
			sb.append("           'Y' ");
			sb.append("        end) as \"COL_CAN_NULL\", ");
			sb.append("        (case f.pk_name ");
			sb.append("          when '$$$' then ");
			sb.append("           'N' ");
			sb.append("          else ");
			sb.append("           'Y' ");
			sb.append("        end) as \"COL_IS_PK\", ");
			sb.append("        f.adsrc as \"COL_DEFAULT\" ");
			sb.append("   from (select a.oid, ");
			sb.append("                a.relname, ");
			sb.append("                a.col_nm_jp, ");
			sb.append("                a.col_type_info, ");
			sb.append("                a.col_nm_en, ");
			sb.append("                a.col_can_null, ");
			sb.append("                COALESCE(b.pk_name, '$$$') as pk_name, ");
			sb.append("                pg_attrdef.adsrc ");
			sb.append("           from (select c.oid, ");
			sb.append("                        c.relname, ");
			sb.append("                        col_description(a.attrelid, a.attnum) as col_nm_jp, ");
			sb.append("                        format_type(a.atttypid, a.atttypmod) as col_type_info, ");
			sb.append("                        a.attname as col_nm_en, ");
			sb.append("                        a.attnotnull as col_can_null, ");
			sb.append("                        a.attrelid, ");
			sb.append("                        a.attnum ");
			sb.append("                   from pg_class as c, pg_attribute as a");

			if (CsjStrUtils.isEmpty(objs[1])) {

				sb.append("                   , pg_statio_all_tables as tbs ");
				sb.append("                  where a.attrelid = c.oid ");
				sb.append("                    and a.attrelid = tbs.relid ");
				sb.append("                    and upper(tbs.schemaname) = 'PUBLIC" + "'");
			} else {
				sb.append("                   , pg_statio_all_tables as tbs ");
				sb.append("                  where a.attrelid = c.oid ");
				sb.append("                    and a.attrelid = tbs.relid ");
				sb.append(
						"                    and upper(tbs.schemaname) = '" + CsjStrUtils.toLowOrUpStr(objs[1]) + "'");
			}

			sb.append("                    and a.attnum > 0 ");
			sb.append("                    and a.attname not like '...%' ");

			if (CsjStrUtils.isNotEmpty(objs[0])) {
				sb.append("   AND c.relname in " + objs[0]);
			}

			sb.append("                    ) a ");
			sb.append("           left outer join pg_attrdef ");
			sb.append("             on a.attrelid = pg_attrdef.adrelid ");
			sb.append("            and a.attnum = pg_attrdef.adnum ");
			sb.append("           left outer join (select pg_class.oid, ");
			sb.append("                                  pg_constraint.conname as pk_name, ");
			sb.append("                                  pg_attribute.attname  as colname, ");
			sb.append("                                  pg_type.typname       as typename ");
			sb.append("                             from pg_constraint ");
			sb.append("                            inner join pg_class ");
			sb.append("                               on pg_constraint.conrelid = pg_class.oid ");
			sb.append("                            inner join pg_attribute ");
			sb.append("                               on pg_attribute.attrelid = pg_class.oid ");
			sb.append("                              and (pg_attribute.attnum = pg_constraint.conkey [ 0 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 1 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 2 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 3 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 4 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 5 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 6 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 7 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 8 ] or ");
			sb.append("                                   pg_attribute.attnum = pg_constraint.conkey [ 9 ]) ");
			sb.append("                            inner join pg_type ");
			sb.append(
					"                               on pg_type.oid = pg_attribute.atttypid  where pg_constraint.contype='p') b ");
			sb.append("             on a.oid = b.oid ");
			sb.append("            and a.col_nm_en = b.colname) f ");
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}

		return sb.toString();
	}

	public static String getMysqlTablesInfoSql(Object... objs) throws Throwable {
		StringBuffer sb = new StringBuffer();
		CsjLog4j.logger.debug("AutoDbToXls.getMysqlTablesInfoSql(Object... objs) begin");
		try {

			sb.append(" SELECT TABLE_NAME       TBL_NM_EN, ");
			sb.append("        ORDINAL_POSITION COL_ID, ");
			sb.append("        COLUMN_COMMENT   COL_NM_JP, ");
			sb.append("        COLUMN_NAME      COL_NM_EN, ");
			sb.append("         ");
			sb.append("        COLUMN_DEFAULT COL_DEFAULT, ");
			sb.append("         ");
			sb.append("        DATA_TYPE COL_TYPE, ");
			sb.append("        COLUMN_TYPE COL_TYPE_INFO, ");
			sb.append("        CASE ");
			sb.append("          WHEN COLUMN_KEY = 'PRI' THEN ");
			sb.append("           'Y' ");
			sb.append("          ELSE ");
			sb.append("           'N' ");
			sb.append("        END COL_IS_PK, ");
			sb.append("        CASE ");
			sb.append("          WHEN IS_NULLABLE = 'YES' THEN ");
			sb.append("           'Y' ");
			sb.append("          ELSE ");
			sb.append("           'N' ");
			sb.append("        END COL_CAN_NULL, ");
			sb.append("        CHARACTER_MAXIMUM_LENGTH, ");
			sb.append("        EXTRA, ");
			sb.append("        NUMERIC_PRECISION ");
			sb.append("   FROM \"information_schema\".\"COLUMNS\" ");

			if (CsjStrUtils.isNotEmpty(objs)) {

				sb.append("  WHERE UPPER(TABLE_SCHEMA) = UPPER('" + objs[0] + "') ");
				if (CsjStrUtils.isNotEmpty(objs[1])) {
					sb.append(" AND  TABLE_NAME in ");
					sb.append(objs[1]);
				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}
		CsjLog4j.logger.debug("AutoDbToXls.getMysqlTablesInfoSql(Object... objs) end");
		return sb.toString();
	}

	public static String getDb2TablesInfoSql(Object... objs) throws Throwable {
		StringBuffer sb = new StringBuffer();
		CsjLog4j.logger.debug("AutoDbToXls.getDb2TablesInfoSql(Object... objs) begin");
		try {
			sb.append("SELECT TABNAME TBL_NM_EN, ");
			sb.append("       COLNO COL_ID, ");
			sb.append("       REMARKS COL_NM_JP, ");
			sb.append("       COLNAME COL_NM_EN, ");
			sb.append("       COL_DEFAULT, ");
			sb.append("       TYPENAME COL_TYPE, ");
			sb.append("       CASE ");
			sb.append("         WHEN TYPENAME = 'CHARACTER' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ')' ");
			sb.append("         WHEN TYPENAME = 'VARCHAR' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ')' ");
			sb.append("         WHEN TYPENAME = 'DECIMAL' THEN ");
			sb.append("          TYPENAME || '(' || TRIM(CHAR(COL_LEN)) || ',' || TRIM(CHAR(SCALE)) || ')' ");
			sb.append("         ELSE ");
			sb.append("          TYPENAME ");
			sb.append("       END COL_TYPE_INFO, ");
			sb.append("       CASE ");
			sb.append("         WHEN KEYSEQ IS NOT NULL THEN ");
			sb.append("          'Y' ");
			sb.append("         ELSE ");
			sb.append("          'N' ");
			sb.append("       END COL_IS_PK, ");
			sb.append("       NULLS COL_CAN_NULL, ");
			sb.append("       SCALE NUMERIC_PRECISION, ");
			sb.append("       TABSCHEMA ");
			sb.append("  FROM (SELECT TABSCHEMA, ");
			sb.append("               TABNAME, ");
			sb.append("               COLNAME, ");
			sb.append("               COLNO, ");
			// sb.append(" TYPESCHEMA, ");
			sb.append("               TYPENAME, ");
			sb.append("               LENGTH             COL_LEN, ");
			sb.append("               SCALE, ");
			sb.append("               DEFAULT            COL_DEFAULT, ");
			sb.append("               NULLS, ");
			// sb.append(" CODEPAGE, ");
			sb.append("               COLLATIONSCHEMA, ");
			// sb.append(" COLLATIONNAME, ");
			// sb.append(" LOGGED, ");
			// sb.append(" COMPACT, ");
			// sb.append(" COLCARD, ");
			// sb.append(" HIGH2KEY, ");
			// sb.append(" LOW2KEY, ");
			// sb.append(" AVGCOLLEN, ");
			sb.append("               KEYSEQ, ");
			// sb.append(" PARTKEYSEQ, ");
			// sb.append(" NQUANTILES, ");
			// sb.append(" NMOSTFREQ, ");
			// sb.append(" NUMNULLS, ");
			// sb.append(" TARGET_TYPESCHEMA, ");
			// sb.append(" TARGET_TYPENAME, ");
			// sb.append(" SCOPE_TABSCHEMA, ");
			// sb.append(" SCOPE_TABNAME, ");
			// sb.append(" SOURCE_TABSCHEMA, ");
			// sb.append(" SOURCE_TABNAME, ");
			// sb.append(" DL_FEATURES, ");
			// sb.append(" SPECIAL_PROPS, ");
			// sb.append(" HIDDEN, ");
			// sb.append(" INLINE_LENGTH, ");
			// sb.append(" PCTINLINED, ");
			// sb.append(" IDENTITY, ");
			// sb.append(" ROWCHANGETIMESTAMP, ");
			// sb.append(" GENERATED, ");
			// sb.append(" TEXT, ");
			// sb.append(" COMPRESS, ");
			// sb.append(" AVGDISTINCTPERPAGE, ");
			// sb.append(" PAGEVARIANCERATIO, ");
			// sb.append(" SUB_COUNT, ");
			// sb.append(" SUB_DELIM_LENGTH, ");
			// sb.append(" AVGCOLLENCHAR, ");
			// sb.append(" IMPLICITVALUE, ");
			// sb.append(" SECLABELNAME, ");
			// sb.append(" ROWBEGIN, ");
			// sb.append(" ROWEND, ");
			// sb.append(" TRANSACTIONSTARTID, ");
			// sb.append(" QUALIFIER, ");
			// sb.append(" FUNC_PATH, ");
			sb.append("               REMARKS ");
			sb.append("          FROM SYSCAT.COLUMNS ");

			if (CsjStrUtils.isNotEmpty(objs)) {

				sb.append("  WHERE 1=1 ");
				if (CsjStrUtils.isNotEmpty(objs[0])) {
					sb.append(" AND UPPER(TABSCHEMA) = UPPER('");
					sb.append(objs[0]);
					sb.append("')");
				}
				if (CsjStrUtils.isNotEmpty(objs[1])) {
					sb.append(" AND  TABNAME in ");
					sb.append(objs[1]);
				}

			}

			sb.append("           ) AAA ");
			sb.append(" ORDER BY COL_ID ");

		} catch (Throwable e) {
			e.printStackTrace();
			CsjLog4j.logger.error(e.getMessage());
			throw e;
		}
		CsjLog4j.logger.debug("AutoDbToXls.getDb2TablesInfoSql(Object... objs) end");
		return sb.toString();
	}

}
