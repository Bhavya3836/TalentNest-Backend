package com.company.TalentNest.Enums;

public enum UserRoles {
    ROLE_ADMIN,
    ROLE_RECRUITER,
    ROLE_CANDIDATE;

    @Override
    public String toString() {
        return name(); // Returns "ROLE_RECRUITER", etc.
    }
}