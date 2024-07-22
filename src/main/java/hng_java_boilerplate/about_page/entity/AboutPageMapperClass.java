package hng_java_boilerplate.about_page.entity;


import org.springframework.stereotype.Component;


@Component
public class AboutPageMapperClass {




        public AboutPageDto mapAboutPageToAboutPageDto(AboutPage entity) {
            if (entity == null) {
                return null;
            }

            AboutPageDto dto = new AboutPageDto();
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setIntroduction(entity.getIntroduction());

            AboutPageDto.CustomSections customSections = new AboutPageDto.CustomSections();
            customSections.setStats(toStatsDto(entity.getCustomSections().getStats()));
            customSections.setServices(toServicesDto(entity.getCustomSections().getServices()));

            dto.setCustomSections(customSections);

            return dto;
        }

        public AboutPage mapAboutPageDtoToAboutPage(AboutPageDto dto) {
            if (dto == null) {
                return null;
            }

            AboutPage entity = new AboutPage();
            entity.setId(dto.getId());
            entity.setTitle(dto.getTitle());
            entity.setIntroduction(dto.getIntroduction());

            AboutPage.CustomSections customSections = new AboutPage.CustomSections();
            customSections.setStats(toStatsEntity(dto.getCustomSections().getStats()));
            customSections.setServices(toServicesEntity(dto.getCustomSections().getServices()));

            entity.setCustomSections(customSections);

            return entity;
        }

        private AboutPageDto.Stats toStatsDto(AboutPage.Stats entity) {
            if (entity == null) {
                return null;
            }

            return new AboutPageDto.Stats(
                    entity.getYearsInBusiness(),
                    entity.getCustomers(),
                    entity.getMonthlyBlogReaders(),
                    entity.getSocialFollowers()
            );
        }

        private AboutPage.Stats toStatsEntity(AboutPageDto.Stats dto) {
            if (dto == null) {
                return null;
            }

            AboutPage.Stats entity = new AboutPage.Stats();
            entity.setYearsInBusiness(dto.getYearsInBusiness());
            entity.setCustomers(dto.getCustomers());
            entity.setMonthlyBlogReaders(dto.getMonthlyBlogReaders());
            entity.setSocialFollowers(dto.getSocialFollowers());

            return entity;
        }

        private AboutPageDto.Services toServicesDto(AboutPage.Services entity) {
            if (entity == null) {
                return null;
            }

            return new AboutPageDto.Services(
                    entity.getTitle(),
                    entity.getDescription()
            );
        }

        private AboutPage.Services toServicesEntity(AboutPageDto.Services dto) {
            if (dto == null) {
                return null;
            }

            AboutPage.Services entity = new AboutPage.Services();
            entity.setTitle(dto.getTitle());
            entity.setDescription(dto.getDescription());

            return entity;
        }
    }

