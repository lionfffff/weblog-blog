package com.yhh.weblogadmin.utils;

import com.yhh.weblogcommon.domain.dos.BlogSettingsDO;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class DefaultAvatarPool {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final List<String> AVATAR_URLS = Arrays.asList(
            "http://localhost:8086/images/avatars/avatar-01.svg",
            "http://localhost:8086/images/avatars/avatar-02.svg",
            "http://localhost:8086/images/avatars/avatar-03.svg",
            "http://localhost:8086/images/avatars/avatar-04.svg",
            "http://localhost:8086/images/avatars/avatar-05.svg",
            "http://localhost:8086/images/avatars/avatar-06.svg",
            "http://localhost:8086/images/avatars/avatar-07.svg",
            "http://localhost:8086/images/avatars/avatar-08.svg"
    );

    private DefaultAvatarPool() {
    }

    public static String pickUnusedAvatar(List<BlogSettingsDO> blogSettingsDOS, String username) {
        Set<String> usedAvatars = blogSettingsDOS.stream()
                .map(BlogSettingsDO::getAvatar)
                .filter(Objects::nonNull)
                .filter(AVATAR_URLS::contains)
                .collect(Collectors.toSet());

        List<String> available = AVATAR_URLS.stream()
                .filter(url -> !usedAvatars.contains(url))
                .collect(Collectors.toList());

        if (!available.isEmpty()) {
            return available.get(RANDOM.nextInt(available.size()));
        }

        int index = Math.abs(Objects.hash(username, System.nanoTime())) % AVATAR_URLS.size();
        return AVATAR_URLS.get(index);
    }
}
