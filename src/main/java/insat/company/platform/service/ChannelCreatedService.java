package insat.company.platform.service;

import insat.company.platform.domain.SlackChannel;
import insat.company.platform.repository.SlackChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.JsonObject;

@Service
public class ChannelCreatedService {

    private final Logger log = LoggerFactory.getLogger(ChannelCreatedService.class);
    @Autowired
    SlackChannelRepository slackChannelRepository;



    public SlackChannel AddChannel(JsonObject objet, String team_id) {

        // Mapping the channel !
        SlackChannel channel = new SlackChannel();
        String channel_id = objet.getString("id");
        channel.setChannelId(channel_id);
        String name = objet.getString("name");
        channel.setName(name);
        channel.setTeamId(team_id);

        if(channel_id.charAt(0) == 'C')
        channel.setIsPrivate(false);
        else
            channel.setIsPrivate(true);

        log.info("Channel mapped !");

        //Saving the channel into Database
        channel = slackChannelRepository.save(channel);
        log.info("Channel saved !");

        return channel;



    }
}
