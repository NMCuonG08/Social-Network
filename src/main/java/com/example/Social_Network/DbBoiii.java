package com.example.Social_Network;

import com.example.Social_Network.User.User;


public class DbBoiii {



    public static String getConvId(User user1, User user2) {
        String id1 = user1.getId().toString();
        String id2 = user2.getId().toString();

        return id1.compareTo(id2) > 0 ? id2 + "_" + id1 : id1 + "_" + id2;
    }
}
