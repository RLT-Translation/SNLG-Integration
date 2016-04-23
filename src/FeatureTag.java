
public enum FeatureTag {
    PAST (FeatureGroup.TenseType),
    PRESENT (FeatureGroup.TenseType),
    FUTURE (FeatureGroup.TenseType),
    
    SING (FeatureGroup.NumberType),
    PLURAL (FeatureGroup.NumberType),
	
	PASSIVE (FeatureGroup.VerbModType),
	PERFECT (FeatureGroup.VerbModType),
	PROGRESSIVE (FeatureGroup.VerbModType);

    private FeatureGroup group;

    FeatureTag(FeatureGroup group) {
        this.group = group;
    }

    public boolean isInFeatureGroup(FeatureGroup group) {
        return this.group == group;
    }
    
    public FeatureGroup getFeatureGroup() {
    	return this.group;
    }

    public enum FeatureGroup {
        TenseType,
        NumberType,
        VerbModType
    }
}
