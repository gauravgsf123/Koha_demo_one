package com.bbbdem.koha.module.my_account.personal_detail

import com.google.gson.annotations.SerializedName

data class PersonalDetailResponseModel(@SerializedName("address"                     ) var address                   : String?  = null,
                                       @SerializedName("address2"                    ) var address2                  : String?  = null,
                                       @SerializedName("altaddress_address"          ) var altaddressAddress         : String?  = null,
                                       @SerializedName("altaddress_address2"         ) var altaddressAddress2        : String?  = null,
                                       @SerializedName("altaddress_city"             ) var altaddressCity            : String?  = null,
                                       @SerializedName("altaddress_country"          ) var altaddressCountry         : String?  = null,
                                       @SerializedName("altaddress_email"            ) var altaddressEmail           : String?  = null,
                                       @SerializedName("altaddress_notes"            ) var altaddressNotes           : String?  = null,
                                       @SerializedName("altaddress_phone"            ) var altaddressPhone           : String?  = null,
                                       @SerializedName("altaddress_postal_code"      ) var altaddressPostalCode      : String?  = null,
                                       @SerializedName("altaddress_state"            ) var altaddressState           : String?  = null,
                                       @SerializedName("altaddress_street_number"    ) var altaddressStreetNumber    : String?  = null,
                                       @SerializedName("altaddress_street_type"      ) var altaddressStreetType      : String?  = null,
                                       @SerializedName("altcontact_address"          ) var altcontactAddress         : String?  = null,
                                       @SerializedName("altcontact_address2"         ) var altcontactAddress2        : String?  = null,
                                       @SerializedName("altcontact_city"             ) var altcontactCity            : String?  = null,
                                       @SerializedName("altcontact_country"          ) var altcontactCountry         : String?  = null,
                                       @SerializedName("altcontact_firstname"        ) var altcontactFirstname       : String?  = null,
                                       @SerializedName("altcontact_phone"            ) var altcontactPhone           : String?  = null,
                                       @SerializedName("altcontact_postal_code"      ) var altcontactPostalCode      : String?  = null,
                                       @SerializedName("altcontact_state"            ) var altcontactState           : String?  = null,
                                       @SerializedName("altcontact_surname"          ) var altcontactSurname         : String?  = null,
                                       @SerializedName("anonymized"                  ) var anonymized                : Boolean? = null,
                                       @SerializedName("autorenew_checkouts"         ) var autorenewCheckouts        : Boolean? = null,
                                       @SerializedName("cardnumber"                  ) var cardnumber                : String?  = null,
                                       @SerializedName("category_id"                 ) var categoryId                : String?  = null,
                                       @SerializedName("check_previous_checkout"     ) var checkPreviousCheckout     : String?  = null,
                                       @SerializedName("city"                        ) var city                      : String?  = null,
                                       @SerializedName("country"                     ) var country                   : String?  = null,
                                       @SerializedName("date_enrolled"               ) var dateEnrolled              : String?  = null,
                                       @SerializedName("date_of_birth"               ) var dateOfBirth               : String?  = null,
                                       @SerializedName("date_renewed"                ) var dateRenewed               : String?  = null,
                                       @SerializedName("email"                       ) var email                     : String?  = null,
                                       @SerializedName("expiry_date"                 ) var expiryDate                : String?  = null,
                                       @SerializedName("fax"                         ) var fax                       : String?  = null,
                                       @SerializedName("firstname"                   ) var firstname                 : String?  = null,
                                       @SerializedName("gender"                      ) var gender                    : String?  = null,
                                       @SerializedName("incorrect_address"           ) var incorrectAddress          : Boolean? = null,
                                       @SerializedName("initials"                    ) var initials                  : String?  = null,
                                       @SerializedName("lang"                        ) var lang                      : String?  = null,
                                       @SerializedName("last_seen"                   ) var lastSeen                  : String?  = null,
                                       @SerializedName("library_id"                  ) var libraryId                 : String?  = null,
                                       @SerializedName("login_attempts"              ) var loginAttempts             : Int?     = null,
                                       @SerializedName("middle_name"                 ) var middleName                : String?  = null,
                                       @SerializedName("mobile"                      ) var mobile                    : String?  = null,
                                       @SerializedName("opac_notes"                  ) var opacNotes                 : String?  = null,
                                       @SerializedName("other_name"                  ) var otherName                 : String?  = null,
                                       @SerializedName("overdrive_auth_token"        ) var overdriveAuthToken        : String?  = null,
                                       @SerializedName("patron_card_lost"            ) var patronCardLost            : Boolean? = null,
                                       @SerializedName("patron_id"                   ) var patronId                  : Int?     = null,
                                       @SerializedName("phone"                       ) var phone                     : String?  = null,
                                       @SerializedName("postal_code"                 ) var postalCode                : String?  = null,
                                       @SerializedName("privacy"                     ) var privacy                   : Int?     = null,
                                       @SerializedName("privacy_guarantor_checkouts" ) var privacyGuarantorCheckouts : Int?     = null,
                                       @SerializedName("privacy_guarantor_fines"     ) var privacyGuarantorFines     : Boolean? = null,
                                       @SerializedName("pronouns"                    ) var pronouns                  : String?  = null,
                                       @SerializedName("relationship_type"           ) var relationshipType          : String?  = null,
                                       @SerializedName("restricted"                  ) var restricted                : Boolean? = null,
                                       @SerializedName("secondary_email"             ) var secondaryEmail            : String?  = null,
                                       @SerializedName("secondary_phone"             ) var secondaryPhone            : String?  = null,
                                       @SerializedName("sms_number"                  ) var smsNumber                 : String?  = null,
                                       @SerializedName("sms_provider_id"             ) var smsProviderId             : String?  = null,
                                       @SerializedName("staff_notes"                 ) var staffNotes                : String?  = null,
                                       @SerializedName("state"                       ) var state                     : String?  = null,
                                       @SerializedName("statistics_1"                ) var statistics1               : String?  = null,
                                       @SerializedName("statistics_2"                ) var statistics2               : String?  = null,
                                       @SerializedName("street_number"               ) var streetNumber              : String?  = null,
                                       @SerializedName("street_type"                 ) var streetType                : String?  = null,
                                       @SerializedName("surname"                     ) var surname                   : String?  = null,
                                       @SerializedName("title"                       ) var title                     : String?  = null,
                                       @SerializedName("updated_on"                  ) var updatedOn                 : String?  = null,
                                       @SerializedName("userid"                      ) var userid                    : String?  = null,
                                       @SerializedName("error"                      ) var error                      : String?  = null)
