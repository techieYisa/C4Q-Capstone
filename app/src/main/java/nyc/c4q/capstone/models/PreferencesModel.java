package nyc.c4q.capstone.models;

/**
 * Created by c4q on 3/31/18.
 */

public class PreferencesModel {

    public String medical, housing, education, children, volunteer, events, community, sports, tragedy;

    public PreferencesModel(){

    }
    public PreferencesModel(String medical, String housing, String education, String children, String volunteer, String events, String community, String sports, String tragedy) {
        this.medical = medical;
        this.housing = housing;
        this.education = education;
        this.children = children;
        this.volunteer = volunteer;
        this.events = events;
        this.community = community;
        this.sports = sports;
        this.tragedy = tragedy;
    }
}
