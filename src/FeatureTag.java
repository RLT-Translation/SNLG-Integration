
public enum FeatureTag {
    PAST (FeatureGroup.TenseType),
    PRESENT (FeatureGroup.TenseType),
    FUTURE (FeatureGroup.TenseType),
    
    SING (FeatureGroup.NumberType),
    PLURAL (FeatureGroup.NumberType),
    
    REG (FeatureGroup.NounModType),
    PROP (FeatureGroup.NounModType),
    PRO (FeatureGroup.NounModType),
	
	PLAIN (FeatureGroup.VerbModType),
	PROGRESSIVE (FeatureGroup.VerbModType),
	
	UNMOD(FeatureGroup.CompModType),
	COMP (FeatureGroup.CompModType),
	SUP (FeatureGroup.CompModType);

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
        VerbModType,
        CompModType,
        NounModType
    }
}
