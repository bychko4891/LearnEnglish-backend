
//    @GetMapping("/phrases-pages")
//    public String translationPairsPages(@RequestParam(value = "page", defaultValue = "0") int page,
//                                        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
//                                        Principal principal,
//                                        Model model) {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Page<MiniStory> translationPairsPages = miniStoryService.getTranslationPairsPages(page, size);
//            if (translationPairsPages.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", translationPairsPages.getTotalPages());
//            }
//            model.addAttribute("translationPairsPages", translationPairsPages.getContent());
//            model.addAttribute("currentPage", page);
//
//            return "admin/miniStories";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrases-pages/new-page-phrases")
//    public String newTranslationPairPage(Principal principal) {
//        if (principal != null) {
//            Long count = miniStoryService.countTranslationPairPages() + 1;
//            return "redirect:/admin-page/phrase/" + count + "/new-page-phrases-create";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrase/{id}/new-page-phrases-create")
//    public String newTranslationPairPageCreate(@PathVariable("id") Long id,
//                                               Model model,
//                                               Principal principal) {
//        if (principal != null) {
//            List<Category> mainTranslationPairsPagesCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.MINI_STORIES);
//            if (mainTranslationPairsPagesCategories != null) {
//                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
//            }
//            MiniStory miniStory = new MiniStory();
//            miniStory.setId(id);
//            miniStory.setName("Enter name");
//            miniStory.setStory("Enter text");
//            model.addAttribute("translationPairsPage", miniStory);
//            model.addAttribute("category", "Відсутня");
//
//            return "admin/miniStoriesEdit";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrase/{id}/page-phrases-edit")
//    public String newTranslationPairPageEdit(@PathVariable("id") Long id,
//                                             Model model,
//                                             Principal principal) {
//        if (principal != null) {
//            List<Category> mainTranslationPairsPagesCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.MINI_STORIES);
//            MiniStory miniStory = miniStoryService.getTranslationPairsPage(id);
//            model.addAttribute("category", "Відсутня");
//            if (miniStory.getCategory() != null) {
//                model.addAttribute("category", miniStory.getCategory().getName());
//            }
//
//            if (mainTranslationPairsPagesCategories != null) {
//                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
//            }
//            model.addAttribute("translationPairsPage", miniStory);
//
//
//            return "admin/miniStoriesEdit";
//        }
//        return "redirect:/login";
//    }
//
//
//    @GetMapping("/images")
//    public String imagesPage(@RequestParam(value = "page", defaultValue = "0") int page,
//                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
//                             Principal principal,
//                             Model model) {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Page<Image> imagesPage = imagesService.getImages(page, size);
//            if (imagesPage.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", imagesPage.getTotalPages());
//            }
//            model.addAttribute("images", imagesPage.getContent());
//            model.addAttribute("currentPage", page);
//
//            return "admin/webImages";
//        }
//        return "redirect:/login";
//    }
//
//}