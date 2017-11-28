package cz.dd4j.ui.gui.c2d;
public enum TileIndoor {

  Axe_png("Axe.png")
  ,Bed_01_png("Bed-01.png")
  ,Bed_02_png("Bed-02.png")
  ,Bookshelf_01_png("Bookshelf-01.png")
  ,Bookshelf_02_png("Bookshelf-02.png")
  ,Bookshelf_03_png("Bookshelf-03.png")
  ,Bookshelf_04_png("Bookshelf-04.png")
  ,Carpet1_01_png("Carpet1-01.png")
  ,Carpet1_02_png("Carpet1-02.png")
  ,Carpet1_03_png("Carpet1-03.png")
  ,Carpet1_04_png("Carpet1-04.png")
  ,Carpet1_05_png("Carpet1-05.png")
  ,Carpet1_06_png("Carpet1-06.png")
  ,Carpet2_01_png("Carpet2-01.png")
  ,Carpet2_02_png("Carpet2-02.png")
  ,Carpet2_03_png("Carpet2-03.png")
  ,Carpet2_04_png("Carpet2-04.png")
  ,Carpet2_05_png("Carpet2-05.png")
  ,Carpet2_06_png("Carpet2-06.png")
  ,Chair_png("Chair.png")
  ,Door_png("Door.png")
  ,Exit_png("Exit.png")
  ,Knight_01_png("Knight-01.png")
  ,Knight_02_png("Knight-02.png")
  ,Sword_01_png("Sword-01.png")
  ,Sword_02_png("Sword-02.png")
  ,Table1_01_png("Table1-01.png")
  ,Table1_02_png("Table1-02.png")
  ,Table1_03_png("Table1-03.png")
  ,Table1_04_png("Table1-04.png")
  ,Table2_01_png("Table2-01.png")
  ,Table2_02_png("Table2-02.png")
  ,Table2_03_png("Table2-03.png")
  ,Table2_04_png("Table2-04.png")
  ,Torch1_01_png("Torch1-01.png")
  ,Torch1_02_png("Torch1-02.png")
  ,Torch2_01_png("Torch2-01.png")
  ,Torch2_02_png("Torch2-02.png")
  ,Torch3_01_png("Torch3-01.png")
  ,Torch3_02_png("Torch3-02.png")
  ,Wall_Brown_Full_png("Wall-Brown-Full.png")
  ,Wall_E_png("Wall-E.png")
  ,Wall_N_png("Wall-N.png")
  ,Wall_NE_png("Wall-NE.png")
  ,Wall_NW_png("Wall-NW.png")
  ,Wall_S_png("Wall-S.png")
  ,Wall_SE_png("Wall-SE.png")
  ,Wall_SW_png("Wall-SW.png")
  ,Wall_W_png("Wall-W.png")
  ;
	
  public static final int tileWidth = 16;
  public static final int tileHeight = 16;

  public final String texture;

  private TileIndoor(String name) {
    texture = name;
  }

}
