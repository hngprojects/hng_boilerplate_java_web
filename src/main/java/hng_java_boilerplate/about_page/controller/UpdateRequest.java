package hng_java_boilerplate.about_page.controller;

import java.util.Map;


public class UpdateRequest {
    private String title;
    private String introduction;
    private CustomSections customSections;

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public CustomSections getCustomSections() {
        return customSections;
    }

    public void setCustomSections(CustomSections customSections) {
        this.customSections = customSections;
    }

    public static class CustomSections {

        private Stats stats;
        private Services services;

        // Getters and Setters

        public Stats getStats() {
            return stats;
        }

        public void setStats(Stats stats) {
            this.stats = stats;
        }

        public Services getServices() {
            return services;
        }

        public void setServices(Services services) {
            this.services = services;
        }
    }

    public static class Stats {

        private int yearsInBusiness;
        private int customers;
        private int monthlyBlogReaders;
        private int socialFollowers;

        // Getters and Setters

        public int getYearsInBusiness() {
            return yearsInBusiness;
        }

        public void setYearsInBusiness(int yearsInBusiness) {
            this.yearsInBusiness = yearsInBusiness;
        }

        public int getCustomers() {
            return customers;
        }

        public void setCustomers(int customers) {
            this.customers = customers;
        }

        public int getMonthlyBlogReaders() {
            return monthlyBlogReaders;
        }

        public void setMonthlyBlogReaders(int monthlyBlogReaders) {
            this.monthlyBlogReaders = monthlyBlogReaders;
        }

        public int getSocialFollowers() {
            return socialFollowers;
        }

        public void setSocialFollowers(int socialFollowers) {
            this.socialFollowers = socialFollowers;
        }
    }

    public static class Services {

        private String title;
        private String description;

        // Getters and Setters

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

