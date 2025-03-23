package com.wzh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("\n" +
                "启动成功 \n"+
                " _______ _________ _______  _______ __________________ _        _______ \n" +
                "(  ____ \\\\__   __/(  ___  )(  ____ )\\__   __/\\__   __/( (    /|(  ____ \\\n" +
                "| (    \\/   ) (   | (   ) || (    )|   ) (      ) (   |  \\  ( || (    \\/\n" +
                "| (_____    | |   | (___) || (____)|   | |      | |   |   \\ | || |      \n" +
                "(_____  )   | |   |  ___  ||     __)   | |      | |   | (\\ \\) || | ____ \n" +
                "      ) |   | |   | (   ) || (\\ (      | |      | |   | | \\   || | \\_  )\n" +
                "/\\____) |   | |   | )   ( || ) \\ \\__   | |   ___) (___| )  \\  || (___) |\n" +
                "\\_______)   )_(   |/     \\||/   \\__/   )_(   \\_______/|/    )_)(_______)\n" +
                "                                                                        ");
    }
}
