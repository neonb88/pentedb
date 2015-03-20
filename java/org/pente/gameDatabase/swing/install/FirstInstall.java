package org.pente.gameDatabase.swing.install;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.pente.database.DBHandler;
import org.pente.database.DerbyDBHandler;

/**
 * @author dweebo
 */
public class FirstInstall {

	private static final String[] coreSqls = new String[] {
		"create table player (pid bigint generated by default as identity primary key, name varchar(100) not null, site_id smallint not null, name_lower varchar(100))",
		"create table game_db (dbid smallint generated by default as identity primary key, name varchar(100) not null, version varchar(20))",
		"create table game_site (sid smallint generated by default as identity primary key, name varchar(100) not null, short_name varchar(10), URL varchar(100))",
		"create table game_event (eid int generated by default as identity primary key, name varchar(100) not null, site_id smallint not null, mailing_list varchar(30), game smallint not null)",
		"create table pente_game (gid bigint generated by default as identity primary key, site_id smallint not null, event_id int not null, round varchar(100), section varchar(100), play_date timestamp not null, timer char(1) not null, rated char(1) not null, initial_time smallint, incremental_time smallint, player1_pid bigint not null, player2_pid bigint not null, player1_rating smallint, player2_rating smallint, winner smallint, player1_type char(1) not null, player2_type char(1) not null, game smallint not null,swapped char(1) not null,private char(1) not null,dbid smallint not null default 1)",
		"create table pente_move (gid bigint not null, move_num smallint not null, next_move smallint not null, hash_key bigint not null, rotation smallint not null, game smallint not null, winner smallint not null, play_date timestamp not null,dbid smallint not null default 1,comments varchar(1024),name varchar(32),type smallint)",
		"call syscs_util.syscs_import_table(null,'GAME_SITE','game_site.txt',null,null,null,0)",
		"call syscs_util.syscs_import_table(null,'GAME_EVENT','game_event.txt',null,null,null,0)",
		"call syscs_util.syscs_import_table(null,'PLAYER','player_slim.txt',null,null,null,0)",
		"alter table game_site alter column sid restart with 5",
		"alter table game_event alter column eid restart with 10000",
		"alter table player alter column pid restart with 60000000000000",
		"alter table pente_game alter column gid restart with 60000000000000",
		"alter table game_db alter column dbid restart with 10",
		"create table plunk_prop (name varchar(64) primary key, value blob)",
		"create table plunk_tree(tree_id bigint primary key generated always as identity, name varchar(128) not null, version varchar(64) not null, creator varchar(64) not null, can_edit_props char(1) not null, last_update_dt timestamp not null, creation_dt timestamp not null)",
		"create table plunk_node(tree_id bigint not null,hash_key bigint not null,parent_hash_key bigint not null,rotation smallint not null,move smallint not null,depth smallint not null,type smallint not null,name varchar(32),comments varchar(1024))",
		"alter table plunk_node add primary key(tree_id, hash_key, depth)",
		"create index nm on player (name_lower, pid)",
		"create index p1 on pente_game (dbid, game, player1_pid)",
		"create index p2 on pente_game (dbid, game, player2_pid)",
		"create index venue on pente_game (dbid, game, site_id, event_id, round, section)",
		"alter table pente_move add primary key(gid, move_num, dbid)",
		"create index gid on pente_move (hash_key, move_num, game, dbid, play_date, gid)",
		"create index nextm on pente_move (hash_key, move_num, game, dbid, next_move, rotation, winner)",
	};
	private static final String[] coreDescs = new String[] {
		"Create: table player",
		"Create: table game_db",
		"Create: table game_site",
		"Create: table game_event",
		"Create: table pente_game",
		"Create: table pente_move",
		"Import: table game_site",
		"Import: table game_event",
		"Import: table player",
		"Alter: table game_site",
		"Alter: table game_event",
		"Alter: table player ",
		"Alter: table game_db",
		"Alter: table pente_game",
		"Create: table plunk_prop",
		"Create: table plunk_tree",
		"Create: table plunk_node",
		"Alter: table plunk_node",
		"Create: index on player",
		"Create: index on pente_game",
		"Create: index on pente_game",
		"Create: index on pente_game",
		"Alter: table pente_move",
		"Create: index on pente_move",
		"Create: index on pente_move"
	};

	private static final String[] penteProSqls = new String[] {
		"call syscs_util.SYSCS_IMPORT_DATA(null,'PENTE_GAME','GID,SITE_ID,EVENT_ID,ROUND,SECTION,PLAY_DATE,TIMER,RATED,INITIAL_TIME,INCREMENTAL_TIME,PLAYER1_PID,PLAYER2_PID,PLAYER1_RATING,PLAYER2_RATING,WINNER,PLAYER1_TYPE,PLAYER2_TYPE,GAME,SWAPPED,PRIVATE', null, 'pente_game_pro.txt',null,null,null,0)",
		"call syscs_util.SYSCS_IMPORT_DATA(null,'PENTE_MOVE','GID,MOVE_NUM,NEXT_MOVE,HASH_KEY,ROTATION,GAME,WINNER,PLAY_DATE',null,'pente_move_pro.txt',null,null,null,0)",
		"insert into game_db values(1, 'Pente Pro', '0.1')"
	};
	private static final String[] penteProDescs = new String[] {
		"Import: table pente_game",
		"Import: table pente_move (takes awhile)",
		"Insert: table game_db"
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int retVal = 0;
		FileWriter out = null;
		DBHandler db = null;
		Connection con = null;
		PreparedStatement stmt = null;
		String toRun[] = args.length == 1 && args[0].equals("pro") ? penteProSqls : coreSqls;
		String desc[] = args.length == 1 && args[0].equals("pro") ? penteProDescs : coreDescs;

		try {
			//out = new FileWriter("firstinstall.log");

			//long startTime = System.currentTimeMillis();
			db = new DerbyDBHandler("db;create=true");
			con = db.getConnection();
			for (int i = 0; i < toRun.length; i++) {
				System.out.println(desc[i]);
				//out.write("Run:" + desc[i] + "\n");
				//long st = System.currentTimeMillis();
				stmt = con.prepareStatement(toRun[i]);
				stmt.execute();
				stmt.close();
				//System.out.println("Finished sql in: " + (System.currentTimeMillis() - st) + " milliseconds");
			}
			//System.out.println("Finished all in: " + (System.currentTimeMillis() - startTime) + " milliseconds");

			//TODO insert version number property

		} catch (Throwable t) {
			t.printStackTrace();
			retVal = 1;
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException s) {}
			}
			if (con != null) {
				try { db.freeConnection(con); } catch (SQLException s) {}
			}
			if (db != null) {
				db.destroy();
			}
			if (out != null) {
				try { out.close(); } catch (IOException io) {}
			}
		}
		System.exit(retVal);
	}
}
