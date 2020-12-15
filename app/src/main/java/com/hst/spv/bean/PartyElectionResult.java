package com.hst.spv.bean;

public class PartyElectionResult {

    private String id;
    private String election_year;
    private String party_leader_ta;
    private String party_leader_en;
    private String seats_won;

    public PartyElectionResult(String id, String election_year, String party_leader_en, String seats_won) {
        this.id = id;
        this.election_year = election_year;
        this.party_leader_en = party_leader_en;
        this.seats_won = seats_won;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getElection_year() {
        return election_year;
    }

    public void setElection_year(String election_year) {
        this.election_year = election_year;
    }

    public String getParty_leader_ta() {
        return party_leader_ta;
    }

    public void setParty_leader_ta(String party_leader_ta) {
        this.party_leader_ta = party_leader_ta;
    }

    public String getParty_leader_en() {
        return party_leader_en;
    }

    public void setParty_leader_en(String party_leader_en) {
        this.party_leader_en = party_leader_en;
    }

    public String getSeats_won() {
        return seats_won;
    }

    public void setSeats_won(String seats_won) {
        this.seats_won = seats_won;
    }
}
