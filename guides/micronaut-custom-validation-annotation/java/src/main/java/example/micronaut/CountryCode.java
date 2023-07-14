package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import jakarta.annotation.Nonnull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Every country code in the world.
 * @see <a href="https://www.itu.int/dms_pub/itu-t/opb/sp/T-SP-E.164D-11-2011-PDF-E.pdf">LIST OF ITU-T RECOMMENDATION E.164 ASSIGNED COUNTRY CODES</a>
 */
public enum CountryCode {
    AFGHANISTAN("93", "Afghanistan"),
    ALBANIA("355", "Albania (Republic of)"),
    ALGERIA("213", "Algeria (People's Democratic Republic of)"),
    AMERICAN_SAMOA("1", "American Samoa"),
    ANDORRA("376", "Andorra (Principality of)"),
    ANGOLA("244", "Angola (Republic of)"),
    ANGUILLA("1", "Anguilla"),
    ANTIGUA_AND_BARBUDA("1", "Antigua and Barbuda"),
    ARGENTINA("54", "Argentine Republic"),
    ARMENIA("374", "Armenia (Republic of)"),
    ARUBA("297", "Aruba"),
    AUSTRALIA("61", "Australia"),
    AUSTRALIAN_EXTERNAL_TERRITORIES("672", "Australian External Territories"),
    AUSTRIA("43", "Austria"),
    AZERBAIJAN("994", "Azerbaijan (Republic of)"),
    BAHAMAS("1", "Bahamas (Commonwealth of the)"),
    BAHRAIN("973", "Bahrain (Kingdom of)"),
    BANGLADESH("880", "Bangladesh (People's Republic of)"),
    BARBADOS("1", "Barbados"),
    BELARUS("375", "Belarus (Republic of)"),
    BELGIUM("32", "Belgium"),
    BELIZE("501", "Belize"),
    BENIN("229", "Benin (Republic of)"),
    BERMUDA("1", "Bermuda"),
    BHUTAN("975", "Bhutan (Kingdom of)"),
    BOLIVIA("591", "Bolivia (Plurinational State of)"),
    BONAIRE_SINT_EUSTATIUS_AND_SABA("599", "Bonaire, Sint Eustatius and Saba"),
    BOSNIA_AND_HERZEGOVINA("387", "Bosnia and Herzegovina"),
    BOTSWANA("267", "Botswana (Republic of)"),
    BRAZIL("55", "Brazil (Federative Republic of)"),
    BRITISH_VIRGIN_ISLANDS("1", "British Version Islands"),
    BRUNEI("673", "Brunei Darussalam"),
    BULGARIA("359", "Bulgaria (Republic of)"),
    BURKINA_FASO("226", "Burkina Faso"),
    BURUNDI("257", "Burundi (Republic of)"),
    CABO_VERDE("238", "Cabo Verde (Republic of)"),
    CAMBODIA("855", "Cambodia (Kingdom of"),
    CAMEROON("237", "Cameroon (Republic of)"),
    CANADA("1", "Canada"),
    CAYMAN_ISLANDS("1", "Cayman Islands"),
    CENTRAL_AFRICAN_REPUBLIC("236", "Central African Republic"),
    CHAD("235", "Chad (Republic of)"),
    CHILE("56", "Chile"),
    CHINA("86", "China (People's Republic of)"),
    COLOMBIA("57", "Colombia (Republic of)"),
    COMOROS("269", "Comoros (Union of the)"),
    CONGO("242", "Congo (Republic of the)"),
    COOK_ISLANDS("682", "Cook Islands"),
    COSTA_RICA("506", "Costa Rica"),
    CROATIA("385", "Croatia (Republic of)"),
    CUBA("53", "Cuba"),
    CURACAO("599", "Curacao"),
    CYPRUS("357", "Cyprus (Republic of)"),
    CZECH_REPUBLIC("420", "Czech Republic"),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO("243", "Democratic Republic of the Congo"),
    DENMARK("45", "Denmark"),
    DISASTER_RELIEF("888", "Telecommunications for Disaster Relief (TDR)"),
    DJIBOUTI("253", "Djibouti (Republic of)"),
    DOMINICA("1", "Dominica (Commonwealth of)"),
    DOMINICAN_REPUBLIC("1", "Dominican Republic"),
    EAST_TIMOR("670", "Timor-Leste (Democratic Republic of)"),
    ECUADOR("593", "Ecuador"),
    EGYPT("20", "Egypt (Arab Republic of)"),
    EL_SALVADOR("503", "El Salvador (Republic of)"),
    EQUATORIAL_GUINEA("240", "Equatorial Guinea (Republic of)"),
    ERITREA("291", "Eritrea"),
    ESTONIA("372", "Estonia (Republic of)"),
    ETHIOPIA("251", "Ethiopia (Federal Democratic Republic of)"),
    FALKLAND_ISLANDS("500", "Falkland Islands (Malvinas)"),
    FAROE_ISLANDS("298", "Faroe Islands"),
    FIJI("679", "Fiji (Republic of"),
    FINLAND("358", "Finland"),
    FRANCE("33", "France"),
    FRENCH_GUIANA("590", "French Guiana (French Department of)"),
    FRENCH_POLYNESIA("689", "French Polynesia"),
    GABON("241", "Gabonese Republic"),
    GAMBIA("220", "Gambia (Republic of)"),
    GEORGIA("995", "Georgia"),
    GERMANY("49", "Germany (Federal Republic of)"),
    GHANA("233", "Ghana"),
    GIBRALTAR("350", "Gibraltar"),
    GMSS("881", "Global Missile Satellite System (GMSS), shared code"),
    GREECE("30", "Greece"),
    GREENLAND("299", "Greenland (Denmark)"),
    GRENADA("1", "Grenada"),
    GROUP_SHARED("388", "Group of countries, shared code"),
    GUADELOUPE("590", "Guadeloupe (French Department of"),
    GUAM("1", "Guam"),
    GUATEMALA("502", "Guatemala (Republic of)"),
    GUINEA("224", "Guinea (Republic of)"),
    GUINEA_BISSAU("245", "Guinnea-Bassau (Republic of)"),
    GUYANA("592", "Guyana"),
    HAITI("509", "Haiti (Republic of)"),
    HONDURAS("504", "Honduras (Republic of)"),
    HONG_KONG("852", "Hong Kong, China"),
    HUNGARY("36", "Hungary (Republic of)"),
    ICELAND("354", "Iceland"),
    INDIA("91", "India (Republic of)"),
    INDONESIA("62", "Indonesia"),
    INMARSAT("870", "Inmarsat SNAC"),
    INTERNATIONAL_FREEPHONE("800", "International Freephone Service"),
    INTERNATIONAL_NETWORKS("882", "International Networks, shared code"), //There is a second entry for 883 with the same name.
    INTERNATIONAL_PREMIUM("979", "International Premium Rate Service (IPRS)"),
    INTERNATIONAL_SHARED("808", "International Shared Cost Service (ISCS)"),
    INTERNATIONAL_TRIAL("991", "Trial of a proposed new international telecommunication public correspondence service, shared code"),
    IRAN("98", "Iran (Islamic Republic of)"),
    IRAQ("964", "Iraq (Republic of)"),
    IRELAND("353", "Ireland"),
    ISRAEL("972", "Israel (State of)"),
    ITALY("39", "Italy"),
    IVORY_COAST("225", "Cote d'Ivoire (Republic of)"),
    JAMAICA("1", "Jamaica"),
    JAPAN("81", "Japan"),
    JORDAN("962", "Jordan (Hashemite Kingdom of)"),
    KAZAKHSTAN("7", "Kazakhstan (Republic of)"),
    KENYA("254", "Kenya (Republic of)"),
    KIRIBATI("686", "Kiribati (Republic of)"),
    KOSOVO("383", "Kosovo"),
    KUWAIT("965", "Kuwait (State of)"),
    KYRGYZSTAN("996", "Kyrgyz Republic"),
    LAOS("856", "Lao People's Democratic Republic"),
    LATVIA("371", "Latvia (Republic of)"),
    LEBANON("961", "Lebanon"),
    LESOTHO("266", "Lesotho (Kingdom of)"),
    LIBERIA("231", "Liberia (Republic of)"),
    LIBYA("218", "Libya"),
    LIECHTENSTEIN("423", "Liechtenstein (Principality of)"),
    LITHUANIA("370", "Lithuania (Republic of)"),
    LUXEMBOURG("352", "Luxembourg"),
    MACAO("853", "Macao, China"),
    MACEDONIA("389", "The Former Yugoslav Republic of Macedonia"),
    MADAGASCAR("261", "Madagascar (Republic of)"),
    MALAWI("265", "Malawi"),
    MALAYSIA("60", "Malaysia"),
    MALDIVES("960", "Maldives (Republic of)"),
    MALI("223", "Mali (Republic of)"),
    MALTA("356", "Malta"),
    MARSHALL_ISLANDS("692", "Marshall Islands (Republic of)"),
    MARTINIQUE("596", "Martinique (French Department of"),
    MAURITANIA("222", "Mauritania (Islamic Republic of)"),
    MAURITIUS("230", "Mauritius (Republic of)"),
    MEXICO("52", "Mexico"),
    MICRONESIA("691", "Micronesia (Federated States of)"),
    MOLDOVA("373", "Moldova (Republic of)"),
    MONACO("377", "Monaco (Principality of)"),
    MONGOLIA("976", "Mongolia"),
    MONTENEGRO("382", "Montenegro (Republic of)"),
    MONTSERRAT("1", "Montserrat"),
    MOROCCO("212", "Morocco (Kingdom of)"),
    MOZAMBIQUE("258", "Mozambique (Republic of)"),
    MYANMAR("95", "Myanmar (The Republic of the Union of)"),
    NAMIBIA("264", "Namibia (Republic of)"),
    NAURU("674", "Nauru (Republic of)"),
    NEPAL("977", "Nepal (Federal Democratic Republic of)"),
    NETHERLANDS("31", "Netherlands (Kingdom of the)"),
    NEW_CALEDONIA("687", "New Caledonia (Territoire francais d'outre-mer)"),
    NEW_ZEALAND("64", "New Zealand"),
    NICARAGUA("505", "Nicaragua"),
    NIGER("227", "Niger (Republic of)"),
    NIGERIA("234", "Nigeria (Federal Republic of)"),
    NIUE("683", "Niue"),
    NORTH_KOREA("850", "Democratic People's Republic of Korea\n"),
    NORTHERN_MARIANA_ISLANDS("1", "Northern Mariana Islands (Commonwealth of the)"),
    NORWAY("47", "Norway"),
    OMAN("968", "Oman (Sultanate of)"),
    PAKISTAN("92", "Pakistan (Islamic Republic of)"),
    PALAU("680", "Palau (Republic of)"),
    PANAMA("507", "Panama (Republic of)"),
    PAPUA_NEW_GUINEA("675", "Papua New Guinea"),
    PARAGUAY("595", "PARAGUAY (Republic of)"),
    PERU("51", "Peru"),
    PHILIPPINES("63", "Philippines (Republic of the)"),
    POLAND("48", "Poland (Republic of)"),
    PORTUGAL("351", "Portugal"),
    PUERTO_RICO("1", "Puerto Rico"),
    QATAR("974", "Qatar (State of)"),
    REUNION("262", "French Departments and Territories in the Indian Ocean"),
    ROMANIA("40", "Romania"),
    RUSSIA("7", "Russian Federation"),
    RWANDA("250", "Rwanda (Republic of)"),
    SAINT_HELENA("290", "Saint Helena, Ascension and the Tristan da Cunha"), //Also, 247 has an identical entry
    SAINT_KITTS_AND_NEVIS("1", "Saint Kitts and Nevis"),
    SAINT_LUCIA("1", "Saint Lucia"),
    SAINT_PIERRE_AND_MIQUELON("508", "Saint Pierre and Miquelon (Collectivite territoriale de la Republique francaise)"),
    SAINT_VINCENT_AND_THE_GRENADINES("1", "Saint Vincent and the Grenadines"),
    SAMOA("685", "Samoa (Independent State of"),
    SAN_MARINO("378", "San Marino (Republic of)"),
    SAO_TOME_AND_PRINCIPE("239", "Sao Tome and Principe (Democratic Republic of)"),
    SAUDI_ARABIA("966", "Saudi Arabia (Kingdom of)"),
    SENEGAL("221", "Senegal (Republic of)"),
    SERBIA("381", "Serbia (Republic of)"),
    SEYCHELLES("248", "Seychelles (Republic of)"),
    SIERRA_LEONE("232", "Sierra Leone"),
    SINGAPORE("65", "Singapore (Republic of)"),
    SINT_MAARTEN("1", "Sint Maarten (Dutch part)"),
    SLOVAKIA("421", "Slovak Republic"),
    SLOVENIA("386", "Slovenia (Republic of)"),
    SOLOMON_ISLANDS("677", "Solomon Islands"),
    SOMALIA("252", "Somalia (Federal Republic of)"),
    SOUTH_AFRICA("27", "South Africa (Republic of)"),
    SOUTH_KOREA("82", "Korea (Republic of)"),
    SOUTH_SUDAN("211", "South Sudan (Republic of)"),
    SPAIN("34", "Spain"),
    SRI_LANKA("94", "Sri Lanka (Democratic Socialist Republic of)"),
    SUDAN("249", "Sudan (Republic of)"),
    SURINAME("597", "Suriname (Republic of"),
    SWAZILAND("268", "Swaziland (Kingdom of)"),
    SWEDEN("46", "Sweden"),
    SWITZERLAND("41", "Switzerland (Confederation of)"),
    SYRIA("963", "Syrian Arab Republic"),
    TAIWAN("886", "Taiwan, China"), //Republic of China?
    TAJIKISTAN("992", "Tajikstan (Republic of)"),
    TANZANIA("255", "Tanzania (United Republic of)"),
    THAILAND("66", "Thailand"),
    TOGO("228", "Togolese Republic"),
    TOKELAU("690", "Tokelau"),
    TONGA("676", "Tonga (Kingdom of)"),
    TRINIDAD_AND_TOBAGO("1", "Trinidad and Tobago"),
    TUNISIA("216", "Tunisia"),
    TURKEY("90", "Turkey"),
    TURKMENISTAN("993", "Turkemenistan"),
    TURKS_AND_CAICOS_ISLANDS("1", "Turks and Caicos Islands"),
    TUVALU("688", "Tuvalu"),
    UGANDA("256", "Uganda (Republic of)"),
    UKRAINE("380", "Ukraine"),
    UNITED_ARAB_EMIRATES("971", "United Arab Emirates"),
    UNITED_KINGDOM("44", "United Kingdom of Great Britain and Northern Ireland"),
    UNITED_STATES("1", "United States of America"),
    UPT("878", "Universal Personal Telecommunication Service (UPT)"),
    URUGUAY("598", "Uruguay (Eastern Republic of)"),
    UZBEKISTAN("998", "Uzbekistan (Republic of)"),
    VANUATU("678", "Vanuatu (Republic of)"),
    VATICAN("379", "Vatican City State"),
    VENEZUELA("58", "Venezuala (Bolivarian Republic of)"),
    VIETNAM("84", "Viet nam (Socialist Republic of)"),
    WALLIS_AND_FUTUNA("681", "Wallis and Futuna (Territoire francais d'outre-mer)"),
    YEMEN("967", "Yemen (Republic of)"),
    ZAMBIA("260", "Zambia (Republic of)"),
    ZIMBABWE("263", "Zimbabwe (Republic of)");

    private String code;
    private String countryName;

    /**
     * Constructor for countries whose name matches their enum.
     * @param code country code
     */
    CountryCode(String code) {
        this.code = code;
    }

    /**
     * Constructor for countries whose name does not match the enum.
     * @param code country code
     * @param countryName full country name
     */
    CountryCode(String code, String countryName) {
        this.code = code;
        this.countryName = countryName;
    }

    public String getCode() {
        return this.code;
    }

    /**
     * Country name.
     * @return country name
     */
    public String getCountryName() {
        return this.countryName;
    }

    private static final List<String> CODES = new ArrayList<>();
    private static final String PLUS_SIGN = "+";

    private static final Map<String, List<CountryCode>> COUNTRYCODESBYCODE = new HashMap<>();

    static {
        Set<String> uniquecodes = new HashSet<>();
        for (CountryCode value : EnumSet.allOf(CountryCode.class)) {
            List<CountryCode> countryCodes = COUNTRYCODESBYCODE.containsKey(value.toString()) ?
                    COUNTRYCODESBYCODE.get(value.toString()) :
                    new ArrayList<>();
            countryCodes.add(value);
            COUNTRYCODESBYCODE.put(value.toString(), countryCodes);
            uniquecodes.add(value.toString());
        }
        CODES.addAll(uniquecodes);
        CODES.sort((Comparator<String>) (s1, s2) -> Integer.compare(s2.length(), s1.length()));

    }

    /**
     *
     * @param code Country code
     * @return a List of {@link CountryCode} for a found code or an empty list
     */
    @NotNull
    @Nonnull
    public static List<CountryCode> countryCodesByCode(@Nonnull @NotBlank String code) {
        if (COUNTRYCODESBYCODE.containsKey(code)) {
            return COUNTRYCODESBYCODE.get(code);
        }
        return new ArrayList<>();
    }

    /**
     *
     * @return Country codes ordered from codes of longer length to less length.
     */
    public static List<String> getCodes() {
        return CODES;
    }

    /**
     *
     * @param number Phone number
     * @return the Country code found in the phone number or {@code null} if not found.
     */
    @Nullable
    public static String parseCountryCode(@Nonnull @NotBlank String number) {
        String phone = number.startsWith(PLUS_SIGN) ? number.substring(1) : number;
        for (String code : getCodes()) {
            if (phone.startsWith(code)) {
                return code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
