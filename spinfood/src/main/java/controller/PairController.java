package controller;
import models.data.PairModel;
import models.service.GroupGeneratorService;
import models.service.PairGeneratorService;

import java.util.List;

public class PairController {

    public static List<PairModel> pairs() {
        var registration = new Registration();
        registration.registerPartyLocation("spinfood/src/Data/partylocation.csv");
        registration.registerParticipants("spinfood/src/Data/teilnehmerliste.csv");
        PairGeneratorService pairGeneratorService = new PairGeneratorService(registration.getParticipants(), registration.getPartyLocation());
        var pairs = pairGeneratorService.generatePairs();
        var groups = new GroupGeneratorService(pairs, registration.getPartyLocation());
        groups.generateGroups();
        return pairs;
    }



    public static void main(String[] args) {
        pairs();
    }
}
