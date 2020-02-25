package io.pixan.systramer.utils;

import java.security.PublicKey;

public class Constants {
    public static final String GOOGLE_BASE = "https://maps.googleapis.com/";


    public static final String ENDPOINT_LOGIN = "login";
    public static final String ENDPOINT_SERVICES = "services";
    public static final String ENDPOINT_FORGOT_PASSWORD = "forgotPassword";
    public static final String ENDPOINT_FILE = "media";
    public static final String ENDPOINT_ALERTS = "services/{serviceId}/alerts";
    public static final String ENDPOINT_PROFILE = "profile";
    public static final String ENDPOINT_ROUTES = "routes/{route_id}";
    public static final String ENDPOINT_INIT_SERVICE = "services/{serviceId}/init";
    public static final String ENDPOINT_FINISH_SERVICE = "services/{serviceId}/finish";
    public static final String ENDPOINT_EVIDENCES = "services/{serviceId}/evidences";
    public static final String ENDPOINT_RESUME_ROUTE = "services/{serviceId}/resume";
    public static final String ENDPOINT_STOP_ROUTE = "services/{serviceId}/stop";

    //Evidences
    public static final int EVIDENCE_TYPE_SEAL = 1;
    public static final int EVIDENCE_TYPE_INCIDENT = 2;
    public static final int EVIDENCE_TYPE_DOCUMENT = 3;
    public static final int EVIDENCE_TYPE_OTHER = 99;

    public static final int ALERT_FINISH_REQUEST_ID = 22;
    public static final int ALERT_CANCEL_REQUEST_ID = 23;

    //Services id
    public static final int SERVICE_STATUS_NEW = 3;
    public static final int SERVICE_STATUS_IN_TRANSIT = 10;
    public static final int SERVICE_STATUS_FINISHED = 20;
    public static final int SERVICE_STATUS_CANCELED = 30;

    public static final String STOPS[] = {
            "Primera",
            "Segunda",
            "Tercera",
            "Cuarta",
            "Quinta",
            "Sexta",
            "Septima",
            "Octava",
            "Novena",
            "Decima",
            "Undécima",
            "Duodécima",
            "Decimotercera",
            "Decimocuarta",
            "Decimoquinta",
            "Decimosexta",
            "Decimoséptima",
            "Decimoctava",
            "Decimonovena",
            "vigésima",
            "vigésimo primera",
            "vigésimo segunda",
            "vigésimo tercera",
            "vigésimo cuarta",
            "vigésimo quinta",
            "vigésimo sexta",
            "vigésimo séptima",
            "vigésimo octava",
            "vigésimo novena",
            "trigésimo",
            "trigésimo primera",
            "trigésimo segunda",
            "trigésimo tercera",
            "trigésimo cuarta",
            "trigésimo quinta",
            "trigésimo sexta",
            "trigésimo séptima",
            "trigésimo octava",
            "trigésimo novena",


    };


    //Google
    public static final String ENDPOINT_TIMES = "maps/api/distancematrix/json";
}
