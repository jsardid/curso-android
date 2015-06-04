package com.csc.jsuarezardid.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.csc.jsuarezardid.mushrooms_local.R;

public class MushroomSQLiteHelper extends SQLiteOpenHelper {

	private Context context;
	private static MushroomSQLiteHelper sInstance;
	private static final String DATABASE_NAME = "DBMushrooms";
	private static final String TABLE_NAME = "Mushrooms";
	private static final int DATABASE_VERSION = 1;

	String sqlCreate = "CREATE TABLE "
			+ TABLE_NAME
			+ " (commonName TEXT UNIQUE, binomialName TEXT, description TEXT, image BLOB)";

	public static synchronized MushroomSQLiteHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new MushroomSQLiteHelper(
					context.getApplicationContext(), DATABASE_NAME, null,
					DATABASE_VERSION);
		}
		return sInstance;
	}

	private MushroomSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);

		insert(db,
				"Yellow Brain",
				"Tremella mesenterica",
				"Tremella mesenterica (common names include yellow brain, golden jelly fungus, yellow trembler, and witches' butter[2]) is a common jelly fungus in the Tremellaceae family of the Agaricomycotina. It is most frequently found on dead but attached and on recently fallen branches, especially of angiosperms, as a parasite of wood decay fungi in the genus Peniophora.[3] The gelatinous, orange-yellow fruit body of the fungus, which can grow up to 7.5 cm (3.0 in) diameter, has a convoluted or lobed surface that is greasy or slimy when damp. It grows in crevices in bark, appearing during rainy weather. Within a few days after rain it dries into a thin film or shriveled mass capable of reviving after subsequent rain. This fungus occurs widely in deciduous and mixed forests and is widely distributed in temperate and tropical regions that include Africa, Asia, Australia, Europe, North and South America. Although considered bland and flavorless, the fungus is edible. Tremella mesenterica produces carbohydrates that are attracting research interest because of their various biological activities.",
				Utils.getScaledByteArrayFromDrawable(context.getResources()
						.getDrawable(R.drawable.seta1)));
		insert(db,
				"Wrinkled Coral",
				"Clavulina rugosa",
				"Clavulina is a genus of fungi in the family Clavulinaceae, in the Cantharelloid clade (order Cantharellales).. Species are characterized by having extensively branched fruit bodies, white spore print, and bisterigmate basidia (often with secondary septation). Branches cylindrical or flattened, blunt, pointed or crested at apex. Hyphae with or without clamps. Basidia cylindrical to narrowly clavate, mostly with two sterigmata which are large and strongly incurved. Spores subspherical or broadly ellipsoid, smooth, thin-walled, each with one large oil drop or guttule. [1] The genus contains approximately 45 species with a worldwide distribution, primarily in tropical regions.[2] Species of Clavulina are mostly ectomycorrhizal. A recent study has identified Clavulina to the genera level as present on Nothofagus menziesii adventitious roots",
				Utils.getScaledByteArrayFromDrawable(context.getResources()
						.getDrawable(R.drawable.seta2)));
		insert(db,
				"Caesars Mushroom",
				"Amanita caesarea",
				"Amanita caesarea, commonly known in English as Caesar's mushroom, is a highly regarded edible mushroom in the genus Amanita, native to southern Europe and North Africa. This mushroom was first described by Giovanni Antonio Scopoli in 1772. This mushroom was a favorite of early rulers of the Roman Empire.\nIt has a distinctive orange cap, yellow gills and stipe. Organic acids have been isolated from this species. Similar orange-capped species occur in North America and India. It was known to and valued by the Ancient Romans, who called it Boletus, a name now applied to a very different type of fungus.",
				Utils.getScaledByteArrayFromDrawable(context.getResources()
						.getDrawable(R.drawable.seta3)));
		insert(db,
				"Fly Amanita",
				"Amanita muscaria",
				"Amanita muscaria, commonly known as the fly agaric or fly amanita, is a mushroom and psychoactive basidiomycete fungus, one of many in the genus Amanita. Native throughout the temperate and boreal regions of the Northern Hemisphere, Amanita muscaria has been unintentionally introduced to many countries in the Southern Hemisphere, generally as a symbiont with pine and birch plantations, and is now a true cosmopolitan species. It associates with various deciduous and coniferous trees.\nThe quintessential toadstool, it is a large white-gilled, white-spotted, usually red mushroom, one of the most recognisable and widely encountered in popular culture. Several subspecies with differing cap colour have been recognised, including the brown regalis (often considered a separate species), the yellow-orange flavivolvata, guessowii, formosa, and the pinkish persicina. Genetic studies published in 2006 and 2008 show several sharply delineated clades that may represent separate species.",
				Utils.getScaledByteArrayFromDrawable(context.getResources()
						.getDrawable(R.drawable.seta4)));

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(sqlCreate);
	}

	
	private void insert(SQLiteDatabase db, String commonName,
			String binomialName, String description, byte[] image) {
		String sql = "INSERT INTO Mushrooms (commonName, binomialName, description, image) VALUES (?,?,?,?)";
		SQLiteStatement insertStmt = db.compileStatement(sql);
		insertStmt.clearBindings();
		insertStmt.bindString(1, commonName);
		insertStmt.bindString(2, binomialName);
		insertStmt.bindString(3, description);
		insertStmt.bindBlob(4, image);
		insertStmt.executeInsert();

	}
}