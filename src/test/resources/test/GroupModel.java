package test;

import java.lang.String;
import java.util.List;

public class GroupModel {
    private List<PersonModel> persons;

    public List<PersonModel> getPersons() {
        return this.persons;
    }

    public void setPersons(List<PersonModel> persons) {
        this.persons = persons;
    }

    public static class PersonModel {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}