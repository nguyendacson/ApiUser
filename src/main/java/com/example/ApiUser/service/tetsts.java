package com.example.ApiUser.service;

public class tetsts {


//    public MovieResponse saveMovieFromApi(String apiUrl) {
//        ApiMovieResponse apiMovieResponse = restTemplate.getForObject(apiUrl, ApiMovieResponse.class);
//
//        if (apiMovieResponse == null || apiMovieResponse.getMovie() == null) {
//            throw new RuntimeException("API không trả dữ liệu movie");
//        }
//
//        MovieResponse movieResponse = apiMovieResponse.getMovie();
//
//        List<EpisodeResponse> episodeResponses = apiMovieResponse.getEpisodeResponseList();
//
//        Movie movie = movieMapper.toMovie(movieResponse);
//
//        movie.setCreated(movieResponse.getCreated().getTime());
//        movie.setModified(movieResponse.getModified().getTime());
//
//        movie.setActors(mapData(
//                movieResponse.getActors(),
//                actorRepository::findByName,
//                dataName -> actorRepository.save(Actor.builder().name(dataName).build()),
//                actorRepository::save
//        ));
//
//        movie.setDirectors(mapData(
//                movieResponse.getDirectors(),
//                directorRepository::findByName,
//                dataName -> directorRepository.save(Director.builder().name(dataName).build()),
//                directorRepository::save
//        ));
//
//        movie.setCategories(mapData(
//                movieResponse.getCategories(),
//                data -> categoryRepository.findById(data.getId()),
//                categoryMapper::toEntity, categoryRepository::save
//        ));
//
//        movie.setCountries(mapData(
//                movieResponse.getCountries(),
//                data -> countryRepository.findById(data.getId()),
//                countryMapper::toEntity, countryRepository::save
//        ));
//
//        Movie saved = movieRepository.save(movie);
//
//        List<Episode> episodes =
//                apiMovieResponse.getEpisodeResponseList().stream().map(epResp -> createOrUpdateEpisodeWithDataMovies(epResp, saved)).toList();
//        /*
//               episodeResponses.stream().map(epResp -> createOrUpdateEpisodeWithDataMovies(epResp, saved)).toList();
//            saved.getEpisodes().clear();
//
//        */
//        saved.getEpisodes().addAll(episodes);
//        return movieMapper.toMovieResponse(movieRepository.save(saved));
//    }

//    private Episode createOrUpdateEpisodeWithDataMovies(EpisodeResponse epResp, Movie movie) {
//        // Tìm Episode theo movieId + episodeNumber
//        Optional<Episode> optionalEp = episodeRepository.findByMovieIdAndServerName(movie.getId(), epResp.getServerName());
//
//        Episode ep;
//        if (optionalEp.isPresent()) {
//            // Nếu Episode đã có trong DB → update
//            ep = optionalEp.get();

    /// /            ep.setName(epResp.getName()); // update thông tin khác nếu muốn
    /// /            ep.setModified(LocalDateTime.now());
//        } else {
//            // Nếu chưa có → tạo mới
//            ep = episodeMapper.toEntity(epResp);
//            ep.setMovie(movie);
//            ep = episodeRepository.save(ep); // save để có id
//        }
//
//        // Danh sách DataMovie name mới từ API
//        Set<String> inputMovieDataNames = epResp.getDataMovies().stream().map(DataMovieResponse::getName).collect(Collectors.toSet());
//
//        // Danh sách DataMovie name đã tồn tại trong DB
//        Set<String> existingNames = dataMovieRepository.findExistingName(inputMovieDataNames, ep.getId());
//
//
//        Episode episode = ep; // final hoặc effectively final
//        // Tạo các DataMovie mới
//        Set<DataMovie> newDataMovies = epResp.getDataMovies().stream().filter(dmResp -> !existingNames.contains(dmResp.getName())).map(dataMovieMapper::toEntity).peek(dm -> dm.setEpisode(episode)).collect(Collectors.toSet());
//
//        // Add vào tập hiện có (không clear())
//        ep.getDataMovies().addAll(newDataMovies);
//
//        return episodeRepository.save(ep);
//    }
}
