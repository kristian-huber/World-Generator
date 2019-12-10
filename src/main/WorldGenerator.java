package main;

public class WorldGenerator {
	private static final float AMPLITUDE = 255;
	private static final int OCTAVES = 5;
	private static final float ROUGHNESS = 0.4f;

	private HeightsGenerator generator;
	private Tile[][] tiles;
	private int size;

	public class Tile {
		public float height;
		public Tile n, s, e, w;
		public float dn, ds, de, dw;
	}

	public WorldGenerator(int size) {
		this.tiles = new Tile[size][size];
		this.size = size;
	}

	public Tile[][] genHeights(int seed) {

		this.generator = new HeightsGenerator(AMPLITUDE, OCTAVES, ROUGHNESS);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				float height = generator.generateHeight(i, j);

				Tile t = new Tile();
				t.height = height;
				if (i > 0) {
					tiles[i - 1][j].e = t;
					t.w = tiles[i - 1][j];
				}
				if (j > 0) {
					tiles[i][j - 1].s = t;
					t.n = tiles[i][j - 1];
				}

				tiles[i][j] = t;
			}
		}

		// Normal thermal looks good with i = 25, d = 10
		// Inverse looks good with i = 2, d = 5
		int iterations = 0;
		float delta = 5;
		for (int i = 0; i < iterations; i++) {
			thermalErode(delta);
		}

		// addBordersX(0.25f);
		// addBordersY(0.25f);

		clampValues();

		// Return the total map
		return tiles;
	}

	public void clampValues() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Tile t = tiles[i][j];

				if (t.height < 0)
					t.height = 0;
				if (t.height > AMPLITUDE)
					t.height = AMPLITUDE;
			}
		}
	}

	public void thermalErode(float dh) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				Tile current = tiles[i][j];
				float highestDelta = 0;
				Tile neighbor = null;
				float delta;

				if (i > 0) {
					delta = current.height - tiles[i - 1][j].height;
					if (delta > highestDelta) {
						highestDelta = delta;
						neighbor = tiles[i - 1][j];
					}
				}

				if (j > 0) {
					delta = current.height - tiles[i][j - 1].height;
					if (delta > highestDelta) {
						highestDelta = delta;
						neighbor = tiles[i][j - 1];
					}
				}

				if (i < size - 1) {
					delta = current.height - tiles[i + 1][j].height;
					if (delta > highestDelta) {
						highestDelta = delta;
						neighbor = tiles[i + 1][j];
					}
				}

				if (j < size - 1) {
					delta = current.height - tiles[i][j + 1].height;
					if (delta > highestDelta) {
						highestDelta = delta;
						neighbor = tiles[i][j + 1];
					}
				}

				if (neighbor != null)
					erodeTiles(current, neighbor, dh);
			}
		}
	}

	private void erodeTiles(Tile t, Tile t2, float dh) {
		float delta = t.height - t2.height;
		float sediment = delta - dh;
		if (delta > dh) {
			t2.height -= sediment;
		}
	}

	public void addBordersX(float borderPercent) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if (i < size * borderPercent) {
					tiles[i][j].height *= (i / (size * borderPercent));
				}

				if (i > size * (1 - borderPercent)) {
					tiles[i][j].height *= ((size - i) / (size * borderPercent));
				}
			}
		}
	}

	public void addBordersY(float borderPercent) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if (j < size * borderPercent) {
					tiles[i][j].height *= (j / (size * borderPercent));
				}

				if (j > size * (1 - borderPercent)) {
					tiles[i][j].height *= ((size - j) / (size * borderPercent));
				}
			}
		}
	}
}
