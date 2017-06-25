package cz.dd4j.utils.config;

public class BidirConfig extends GenericConfig {

	public DirLocation source = new DirLocation();
	
	@Override
	public void assign(GenericConfig from) {
		if (from == null) return;
		super.assign(from);
		if (from instanceof BidirConfig) {
			BidirConfig bidir = (BidirConfig)from;
			if (bidir.source != null) {
				if (bidir.source.dir != null) source.dir = bidir.source.dir;
				if (bidir.source.filePrefix != null) source.filePrefix = bidir.source.filePrefix;
			}			
		}
	}
	
}
