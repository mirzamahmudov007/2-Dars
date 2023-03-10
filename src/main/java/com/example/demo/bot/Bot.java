package com.example.demo.bot;

import com.example.demo.bot.Step.DeleteStep;
import com.example.demo.bot.Step.UserStep;
import com.example.demo.bot.api.PrayTime;
import com.example.demo.bot.btn.InlineBtn;
import com.example.demo.bot.btn.RegionBtn;
import com.example.demo.bot.btn.viloyat.*;
import com.example.demo.model.User;
import com.example.demo.service.About_BotService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@PropertySource("classpath:telegram.properties")
@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;
    private final InlineBtn btn;

    private final PrayTime prayTime;
    private final Navoiy navoiy;
    private final Xorazm xorazm;
    private final UserService userService;
    private final Samarqand samarqand;
    private final RegionBtn regionBtn;
    private final RestTemplate restTemplate;
    private final Namangan namangan;

    private final Buxoro buxoro;
    private final Jizzah jizzah;
    private final Fagona fagona;
    private final Toshkent toshkent;
    private final Qashqadaryo qashqadaryo;
    private final Andijon andijon;

    private final Sirdaryo sirdaryo;

    private final About_BotService aboutBotService;

    public Bot(@Lazy InlineBtn btn, PrayTime prayTime, Navoiy navoiy, Xorazm xorazm, @Lazy UserService userService, Samarqand samarqand, RegionBtn regionBtn, @Lazy RestTemplate restTemplate, @Lazy Namangan namangan, Buxoro buxoro, Jizzah jizzah, @Lazy Fagona fagona, @Lazy Toshkent toshkent, Qashqadaryo qashqadaryo, @Lazy Andijon andijon, Sirdaryo sirdaryo, @Lazy About_BotService aboutBotService) {
        this.btn = btn;
        this.prayTime = prayTime;
        this.navoiy = navoiy;
        this.xorazm = xorazm;
        this.userService = userService;
        this.samarqand = samarqand;
        this.regionBtn = regionBtn;
        this.restTemplate = restTemplate;
        this.namangan = namangan;
        this.buxoro = buxoro;
        this.jizzah = jizzah;
        this.fagona = fagona;
        this.toshkent = toshkent;
        this.qashqadaryo = qashqadaryo;
        this.andijon = andijon;
        this.sirdaryo = sirdaryo;
        this.aboutBotService = aboutBotService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            System.out.println(message.getMessageId());
            User user = userService.findByChatId(message.getChatId());
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/start") && user == null) {
                    if (!userService.existByChatId(message.getChatId())) {
                        User userSave = userService.saveUser(message);
                        user = userSave;
                    }
                    user = userService.update(user, user.getId(), UserStep.select_lang); // step saved
                    sendInlineBtn(user.getChatId()); // inline btn
                    userService.updateOxirgiIsh(user, user.getId(), "/start");
                }
                else if (text.equals("/start") && user!=null) {
                    user = userService.update(user, user.getId(), UserStep.select_lang); // step saved
                    replyRemove(user.getChatId().toString());
                    deleteInlineKeyboard1(message);
                    sendInlineBtn(user.getChatId());
                    userService.updateOxirgiIsh(user, user.getId(), "/start");
                } else if (text.equals("/info_bot")) {
                    sendMessage(user.getChatId().toString(), "Obunachilar soni: " + aboutBotService.getMembers());
                }

                else if (user.getOxirgiIsh().equals(DeleteStep.delete_hududni_oz)) {
                    deleteInlineKeyboard(message);
                } else if (text.equals("Hududni o'zgartirish ??????")) {
                    userService.updateOxirgiIsh(user, user.getId(), DeleteStep.delete_hududni_oz);
                    replyRemove(user.getChatId().toString());
                    deleteInlineKeyboard1(message);
                    getRegionInline(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt);
                    deleteInlineKeyboard(message);
                } else if (text.equals("KRIL yozuviga o'tish \uD83D\uDD04") && user.getOxirgiIsh().equals(DeleteStep.delete_krl_ga_otish)) {
                    deleteInlineKeyboard(message);
                } else if (text.equals("KRIL yozuviga o'tish \uD83D\uDD04") && !user.getOxirgiIsh().equals(DeleteStep.delete_krl_ga_otish)) {
                    userService.updateOxirgiIsh(user, user.getId(), DeleteStep.delete_krl_ga_otish);
                    String lt1 = user.getStep().substring(0, 11);
                    String lt2 = user.getStep().substring(15, user.getStep().length());
                    MuvaffaqiyatliKrl(user.getChatId(), user.getData());
//                        sendInlineKeyBoardRegionK(user.getChatId());
                    userService.update(user, user.getId(), lt1 + "_krl_" + lt2);
                    deleteInlineKeyboard(message);
                    System.out.println(user.getStep());
                } else if (text.equals("?????????? ?????????????? ????????\uD83D\uDD04") && user.getOxirgiIsh().equals(DeleteStep.delete_lt_ga_otish)) {
                    deleteInlineKeyboard(message);
                } else if (text.equals("?????????? ?????????????? ????????\uD83D\uDD04") && !user.getOxirgiIsh().equals(DeleteStep.delete_lt_ga_otish)) {
                    userService.updateOxirgiIsh(user, user.getId(), DeleteStep.delete_lt_ga_otish);
                    MuvaffaqiyatliLt(user.getChatId(), user.getData());
                    String krl1 = user.getStep().substring(0, 11);
                    String krl2 = user.getStep().substring(16, user.getStep().length());
                    userService.update(user, user.getId(), krl1 + "_lt_" + krl2);
                    System.out.println(user.getStep());
                    deleteInlineKeyboard(message);
                } else if (text.equals("?????????????? ??????????????????????????")) {
                    userService.updateOxirgiIsh(user, user.getId(), "?????????????? ??????????????????????????");
//                    kllLt(user.getChatId());
                    replyRemove(user.getChatId().toString());
                    deleteInlineKeyboard1(message);
                    sendInlineKeyBoardRegionK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl);
                    deleteInlineKeyboard(message, message.getMessageId());
                } else if (text.startsWith("??? Bugungi namoz vaqtlari")) {
                    if (user.getStep().equals(UserStep.select_lang_lt_location)){
                        sendMessage(user.getChatId().toString(), setApi(user.getLat(), user.getLang(), "Siz turgan joy"));
                    }
                    else if (user.getStep().equals(UserStep.select_lang_lt_namangan)) {
                        if (user.getData().equals("Chust")) { // 41.00615643758215, 71.23771796120751
                            sendMessage(user.getChatId().toString(), setApi(40.999931, 71.236137, "Namangan, Chust tuman"));
                        } else if (user.getData().equals("Namangan")) { // Namangan shahar
                            sendMessage(user.getChatId().toString(), setApi(41.007162, 71.643712, "Namangan, Namangan shahar"));
                        } else if (user.getData().equals("Kosonsoy")) { // Kosonsoy shahar
                            sendMessage(user.getChatId().toString(), setApi(41.19608642817571, 71.52652970907708, "Namangan, Kosonsoy"));
                        } else if (user.getData().equals("Chortoq")) { // Chortoq shahar 41.06368431981446, 71.8230565658223
                            sendMessage(user.getChatId().toString(), setApi(41.06368431981446, 71.8230565658223, "Namangan, Chortoq"));
                        } else if (user.getData().equals("Norin")) { // Namangan shahar 40.940509509996495, 72.00654567901067
                            sendMessage(user.getChatId().toString(), setApi(40.940509509996495, 72.00654567901067, "Namangan, Norin"));
                        } else if (user.getData().equals("Mingbuloq")) { // Namangan shahar 40.78917645308457, 71.38875951462421
                            sendMessage(user.getChatId().toString(), setApi(40.78917645308457, 71.38875951462421, "Namangan, Mingbuloq"));
                        } else if (user.getData().equals("Pop")) { // Namangan shahar 41.0364117324324, 70.75487013756214
                            sendMessage(user.getChatId().toString(), setApi(41.0364117324324, 70.75487013756214, "Namangan, Pop"));
                        } else if (user.getData().equals("To'raqo'rg'on")) { // Namangan shahar 40.99316007632018, 71.51626227933346
                            sendMessage(user.getChatId().toString(), setApi(40.99316007632018, 71.51626227933346, "Namangan, To'raqo'rg'on"));
                        } else if (user.getData().equals("Uchqo'rg'on")) { // Namangan shahar 41.12068842248576, 72.08711447153537
                            sendMessage(user.getChatId().toString(), setApi(41.12068842248576, 72.08711447153537, "Namangan, Uchqo'rg'on"));
                        } else if (user.getData().equals("uychi")) { // Namangan shahar 41.06721589284562, 71.92292974977094
                            sendMessage(user.getChatId().toString(), setApi(71.92292974977094, 71.92292974977094, "Namangan, Uychi"));
                        } else if (user.getData().equals("yangiqorgon")) { // Namangan shahar 41.198968289144275, 71.71703917988339
                            sendMessage(user.getChatId().toString(), setApi(41.198968289144275, 71.71703917988339, "Namangan, Yangoqo'rg'on"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_toshkent)) {
                        if (user.getData().equals("ToshknetSH")) { // 41.31862648082324, 69.26185247247427
                            sendMessage(user.getChatId().toString(), setApi(41.31862648082324, 69.26185247247427, "Toshkent ,Toshkent Shahar"));
                        } else if (user.getData().equals("chilonzor")) { // 41.25966068034852, 69.17981683014095
                            sendMessage(user.getChatId().toString(), setApi(41.25966068034852, 69.17981683014095, "Toshkent ,Chilonzor"));
                        } else if (user.getData().equals("quyiC")) { // 41.324249737732416, 69.27336159543128
                            sendMessage(user.getChatId().toString(), setApi(41.324249737732416, 69.27336159543128, "Toshkent ,Quyi Chirchiq"));
                        } else if (user.getData().equals("bostonliq")) { // 41.29962474317497, 69.24013777030345
                            sendMessage(user.getChatId().toString(), setApi(41.29962474317497, 69.24013777030345, "Toshkent ,Bo'stonliq"));
                        } else if (user.getData().equals("yangiyol")) { // 41.106601409336605, 69.00099564918969
                            sendMessage(user.getChatId().toString(), setApi(41.106601409336605, 69.00099564918969, "Toshkent ,Yangiyo'l"));
                        } else if (user.getData().equals("zangota")) { // 41.19119250475624, 69.14481581613583
                            sendMessage(user.getChatId().toString(), setApi(41.19119250475624, 69.14481581613583, "Toshkent ,Zangiota"));
                        } else if (user.getData().equals("ohangaron")) { // 40.91797520041142, 69.63525266877876
                            sendMessage(user.getChatId().toString(), setApi(40.91797520041142, 69.63525266877876, "Toshkent ,Ohangaron"));
                        } else if (user.getData().equals("oqqq")) { // 40.88044288436734, 69.04266437650311
                            sendMessage(user.getChatId().toString(), setApi(40.88044288436734, 69.04266437650311, "Toshkent ,Oqqo'rg'on"));
                        } else if (user.getData().equals("ppp")) { // 41.297497902393545, 69.68451728729993
                            sendMessage(user.getChatId().toString(), setApi(41.297497902393545, 69.68451728729993, "Toshkent ,Parkent"));
                        } else if (user.getData().equals("ortachirchiq")) { // 41.20590993388904, 69.27922461238849
                            sendMessage(user.getChatId().toString(), setApi(41.20590993388904, 69.27922461238849, "Toshkent ,O'rta Chirchiq"));
                        } else if (user.getData().equals("yuqorichirchiq")) { // 41.20911424680261, 69.49407488275496
                            sendMessage(user.getChatId().toString(), setApi(41.20911424680261, 69.49407488275496, "Toshkent ,Yuqori Chirchiq"));
                        } else if (user.getData().equals("bekobod")) { // 40.454055968860445, 69.1819780480247
                            sendMessage(user.getChatId().toString(), setApi(40.454055968860445, 69.1819780480247, "Toshkent ,Bekobod"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_fargona)) {
                        if (user.getData().equals("Farg???ona shahar")) { //40.37949214005235, 71.78153222147638
                            sendMessage(user.getChatId().toString(), setApi(40.37949214005235, 71.78153222147638, "Farg'ona, Farg???ona shahar"));
                        } else if (user.getData().equals("Uchko???prik")) { //40.53267491097347, 71.02892566146784
                            sendMessage(user.getChatId().toString(), setApi(40.53267491097347, 71.02892566146784, "Farg'ona, Uchko???prik"));
                        } else if (user.getData().equals("So???x")) { //39.968927659480165, 71.12908694248515
                            sendMessage(user.getChatId().toString(), setApi(39.968927659480165, 71.12908694248515, "Farg'ona, So???x"));
                        } else if (user.getData().equals("Yozyovon")) { //40.62131813678478, 71.6480687836224
                            sendMessage(user.getChatId().toString(), setApi(40.62131813678478, 71.6480687836224, "Farg'ona, Yozyovon"));
                        } else if (user.getData().equals("Quva")) { //40.524573367810504, 72.06257627873157
                            sendMessage(user.getChatId().toString(), setApi(40.524573367810504, 72.06257627873157, "Farg'ona, Quva"));
                        } else if (user.getData().equals("Qo???qon")) { //40.524621966621304, 70.93356479162965
                            sendMessage(user.getChatId().toString(), setApi(40.524621966621304, 70.93356479162965, "Farg'ona, Qo???qon"));
                        } else if (user.getData().equals("Beshariq")) { //40.39690886265036, 70.57359975866977
                            sendMessage(user.getChatId().toString(), setApi(40.39690886265036, 70.57359975866977, "Farg'ona, Beshariq"));
                        } else if (user.getData().equals("Quvasoy")) { //
                            sendMessage(user.getChatId().toString(), setApi(40.30222, 71.97444, "Farg'ona, Quvasoy"));
                        } else if (user.getData().equals("Farg???ona")) { //40.320714145540286, 71.70209565835899
                            sendMessage(user.getChatId().toString(), setApi(40.320714145540286, 71.70209565835899, "Farg'ona, Farg???ona"));
                        } else if (user.getData().equals("Toshloq")) { //40.56269866588281, 71.84044642257277
                            sendMessage(user.getChatId().toString(), setApi(40.56269866588281, 71.84044642257277, "Farg'ona, Toshloq"));
                        } else if (user.getData().equals("O???zbekiston")) { //40.330297796679396, 70.85868054864669
                            sendMessage(user.getChatId().toString(), setApi(40.330297796679396, 70.85868054864669, "Farg'ona, O???zbekiston"));
                        } else if (user.getData().equals("Qo???shtepa")) { //40.53692345413236, 71.6415020647065
                            sendMessage(user.getChatId().toString(), setApi(40.53692345413236, 71.6415020647065, "Farg'ona, Qo???shtepa"));
                        } else if (user.getData().equals("Marg???ilon")) { //40.47635685369382, 71.71252435127899
                            sendMessage(user.getChatId().toString(), setApi(40.47635685369382, 71.71252435127899, "Farg'ona, Marg???ilon"));
                        } else if (user.getData().equals("Buvayda")) { //40.645693110093404, 71.10204505839337
                            sendMessage(user.getChatId().toString(), setApi(40.645693110093404, 71.10204505839337, "Farg'ona, Buvayda"));
                        } else if (user.getData().equals("Dang???ara")) { //40.58139710281787, 70.91477380795894
                            sendMessage(user.getChatId().toString(), setApi(40.58139710281787, 70.91477380795894, "Farg'ona, Dang???ara"));
                        } else if (user.getData().equals("Oltiariq")) { //40.39370084174377, 71.48670380912914
                            sendMessage(user.getChatId().toString(), setApi(40.39370084174377, 71.48670380912914, "Farg'ona, Oltiariq"));
                        } else if (user.getData().equals("Rishton")) { //40.36410679785719, 71.27208639645364
                            sendMessage(user.getChatId().toString(), setApi(40.36410679785719, 71.27208639645364, "Farg'ona, Rishton"));
                        } else if (user.getData().equals("Furqat")) { //40.51673390388502, 70.71177286893743
                            sendMessage(user.getChatId().toString(), setApi(40.51673390388502, 70.71177286893743, "Farg'ona, Furqat"));
                        } else if (user.getData().equals("Bag???dod")) { //40.49387968871484, 71.20366862141532
                            sendMessage(user.getChatId().toString(), setApi(40.49387968871484, 71.20366862141532, "Farg'ona, Bag???dod"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_andijon)) {
                        if (user.getData().equals("Andijon shahri")) {//40.7865585435806, 72.31208083702941
                            sendMessage(user.getChatId().toString(), setApi(40.7865585435806, 72.31208083702941, "Andijon, Andijon shahar"));
                        } else if (user.getData().equals("Xonobod")) { //40.79989825348597, 72.98951560333182
                            sendMessage(user.getChatId().toString(), setApi(40.454055968860445, 72.98951560333182, "Andijon, Xonobod"));
                        } else if (user.getData().equals("Asaka")) {// 40.6619355989725, 72.24428848141633
                            sendMessage(user.getChatId().toString(), setApi(40.6619355989725, 72.24428848141633, "Andijon, Asaka"));
                        } else if (user.getData().equals("Baliqchi")) {//40.8681067477643, 71.90901428107483
                            sendMessage(user.getChatId().toString(), setApi(40.8681067477643, 71.90901428107483, "Andijon, Baliqchi"));
                        } else if (user.getData().equals("Bo'z")) { //40.67522368198782, 71.90202999274003
                            sendMessage(user.getChatId().toString(), setApi(40.67522368198782, 71.90202999274003, "Andijon, Bo'z"));
                        } else if (user.getData().equals("Buloqboshi")) { //40.61824097722169, 72.4589485266868
                            sendMessage(user.getChatId().toString(), setApi(40.61824097722169, 72.4589485266868, "Andijon, Buloqboshi"));
                        } else if (user.getData().equals("Izboskan")) { //40.926914438296166, 72.23046114826215
                            sendMessage(user.getChatId().toString(), setApi(40.926914438296166, 72.23046114826215, "Andijon, Izboskan"));
                        } else if (user.getData().equals("Jalolquduq")) {//40.73260859991856, 72.61228519306135
                            sendMessage(user.getChatId().toString(), setApi(40.73260859991856, 72.61228519306135, "Andijon, Jalolquduq"));
                        } else if (user.getData().equals("Marhamat")) { //40.51393917604782, 72.29981218880005
                            sendMessage(user.getChatId().toString(), setApi(40.51393917604782, 72.29981218880005, "Andijon, Marhamat"));
                        } else if (user.getData().equals("Oltinko'l")) {// 40.78257767662204, 72.15630346953014
                            sendMessage(user.getChatId().toString(), setApi(40.78257767662204, 72.15630346953014, "Andijon, Oltinko'l"));
                        } else if (user.getData().equals("Paxtaobod")) { //40.977855409832266, 72.42257164080198
                            sendMessage(user.getChatId().toString(), setApi(40.977855409832266, 72.42257164080198, "Andijon, Paxtaobod"));
                        } else if (user.getData().equals("Qo'rg'ontepa")) { // 40.737458816612616, 72.80489172467401
                            sendMessage(user.getChatId().toString(), setApi(40.737458816612616, 72.80489172467401, "Andijon, Qo'rg'ontepa"));
                        } else if (user.getData().equals("Ulug'nor")) { //40.7812938834895, 71.65829940832162
                            sendMessage(user.getChatId().toString(), setApi(40.7812938834895, 71.65829940832162, "Andijon, Ulug'nor"));
                        } else if (user.getData().equals("Xo'jaobod")) { // 40.644265790848365, 72.59695277036937
                            sendMessage(user.getChatId().toString(), setApi(40.644265790848365, 72.59695277036937, "Andijon, Xo'jaobod"));
                        } else if (user.getData().equals("Shahrixon")) { //40.705658566969085, 72.04954603823948
                            sendMessage(user.getChatId().toString(), setApi(40.705658566969085, 72.04954603823948, "Andijon, Shahrixon"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_samarqand)) {
                        if (user.getData().equals("Samarqand shahri")) {//39.65329327101519, 66.95826234725476
                            sendMessage(user.getChatId().toString(), setApi(39.65329327101519, 66.95826234725476, "Samarqand, Samarqand shahri"));
                        } else if (user.getData().equals("Bulung'ur")) {//39.70791766177822, 67.28142857964608
                            sendMessage(user.getChatId().toString(), setApi(39.70791766177822, 67.28142857964608, "Samarqand, Bulung'ur"));
                        } else if (user.getData().equals("Ishtixon")) {//39.98829349793204, 66.53144028860048
                            sendMessage(user.getChatId().toString(), setApi(39.98829349793204, 66.53144028860048, "Samarqand, Ishtixon"));
                        } else if (user.getData().equals("Jomboy")) {//39.76625238583371, 67.07779535428304
                            sendMessage(user.getChatId().toString(), setApi(39.76625238583371, 67.07779535428304, "Samarqand, Jomboy"));
                        } else if (user.getData().equals("Kattaqo'rg'on tumani")) { // 39.981981100516265, 66.2896029199572
                            sendMessage(user.getChatId().toString(), setApi(39.981981100516265, 66.2896029199572, "Samarqand, Kattaqo'rg'on shahri"));
                        } else if (user.getData().equals("Kattaqo'rg'on shahri")) { // 39.8993149735457, 66.2633149577346
                            sendMessage(user.getChatId().toString(), setApi(39.8993149735457, 66.2633149577346, "Samarqand, Kattaqo'rg'on tumani"));
                        } else if (user.getData().equals("Narpay")) {//39.92524679769102, 65.98627826494945
                            sendMessage(user.getChatId().toString(), setApi(39.92524679769102, 65.98627826494945, "Samarqand, Narpay"));
                        } else if (user.getData().equals("Nurobod")) {//39.57127098105206, 66.02799000748902
                            sendMessage(user.getChatId().toString(), setApi(39.57127098105206, 66.02799000748902, "Samarqand, Nurobod"));
                        } else if (user.getData().equals("Oqdaryo")) {//39.839873784601465, 66.73834694617673
                            sendMessage(user.getChatId().toString(), setApi(39.839873784601465, 66.73834694617673, "Samarqand, Oqdaryo"));
                        } else if (user.getData().equals("Past darg'om")) { // 39.704352292734065, 66.62242053041332
                            sendMessage(user.getChatId().toString(), setApi(39.704352292734065, 66.62242053041332, "Samarqand, Past darg'om"));
                        } else if (user.getData().equals("Urgut")) {//39.447002526177904, 67.17173665735707
                            sendMessage(user.getChatId().toString(), setApi(39.447002526177904, 67.17173665735707, "Samarqand, Urgut"));
                        } else if (user.getData().equals("Paxtachi")) {//39.888515759299004, 65.6089318735053
                            sendMessage(user.getChatId().toString(), setApi(39.888515759299004, 65.6089318735053, "Samarqand, Paxtachi"));
                        } else if (user.getData().equals("Poyariq")) {//40.05104556455984, 66.85419779064077
                            sendMessage(user.getChatId().toString(), setApi(40.05104556455984, 66.85419779064077, "Samarqand, Poyariq"));
                        } else if (user.getData().equals("Samarqand tumani")) { //39.59818617036431, 66.90471762188967
                            sendMessage(user.getChatId().toString(), setApi(39.59818617036431, 66.90471762188967, "Samarqand, Samarqand tumani"));
                        } else if (user.getData().equals("Toyloq")) { //39.56442359774873, 67.14018598905683
                            sendMessage(user.getChatId().toString(), setApi(39.56442359774873, 67.14018598905683, "Samarqand, Toyloq"));
                        } else if (user.getData().equals("Qo'shrabot")) { //40.38655726843597, 66.5100175021684
                            sendMessage(user.getChatId().toString(), setApi(40.38655726843597, 66.5100175021684, "Samarqand, Qo'shrabot"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_jizzah)) {
                        if (user.getData().equals("Jizzax shahri")) {//40.11977007458388, 67.85232343283624
                            sendMessage(user.getChatId().toString(), setApi(40.11977007458388, 67.85232343283624, "Jizzah, Jizzax shahri"));
                        } else if (user.getData().equals("Arnasoy")) { //40.59901232957254, 67.79358090175431
                            sendMessage(user.getChatId().toString(), setApi(40.59901232957254, 67.79358090175431, "Jizzah, Arnasoy"));
                        } else if (user.getData().equals("Baxmal")) {//39.78225382213002, 67.7250016175458
                            sendMessage(user.getChatId().toString(), setApi(39.78225382213002, 67.7250016175458, "Jizzah, Baxmal"));
                        } else if (user.getData().equals("Forish")) {//40.70901562393393, 67.18181520202268
                            sendMessage(user.getChatId().toString(), setApi(40.70901562393393, 67.18181520202268, "Jizzah, Forish"));
                        } else if (user.getData().equals("G'allaorol")) { //40.10205495202923, 67.32846413281408
                            sendMessage(user.getChatId().toString(), setApi(40.10205495202923, 67.32846413281408, "Jizzah, G'allaorol"));
                        } else if (user.getData().equals("Do'stlik")) { //40.58181976770775, 68.04548305263552
                            sendMessage(user.getChatId().toString(), setApi(40.58181976770775, 68.04548305263552, "Jizzah, Do'stlik"));
                        } else if (user.getData().equals("Jizzax tumani")) { //40.11455358141378, 67.82473708142044
                            sendMessage(user.getChatId().toString(), setApi(40.11455358141378, 67.82473708142044, "Jizzah, Jizzax tumani"));
                        } else if (user.getData().equals("Mirzacho'l")) {//40.724412215707, 68.06118184566003
                            sendMessage(user.getChatId().toString(), setApi(40.724412215707, 68.06118184566003, "Jizzah, Mirzacho'l"));
                        } else if (user.getData().equals("Paxtakor")) { //40.35950221136383, 68.00475878330718
                            sendMessage(user.getChatId().toString(), setApi(40.35950221136383, 68.00475878330718, "Jizzah, Paxtakor"));
                        } else if (user.getData().equals("Yangiobod")) {//39.99243484998208, 68.74527110184545
                            sendMessage(user.getChatId().toString(), setApi(39.99243484998208, 68.74527110184545, "Jizzah, Yangiobod"));
                        } else if (user.getData().equals("Zafarobod")) { //40.41330581212753, 67.76451866402623
                            sendMessage(user.getChatId().toString(), setApi(40.41330581212753, 67.76451866402623, "Jizzah, Zafarobod"));
                        } else if (user.getData().equals("Zarband")) { //40.189983986463055, 68.11455808514548
                            sendMessage(user.getChatId().toString(), setApi(40.189983986463055, 68.11455808514548, "Jizzah, Zarband"));
                        } else if (user.getData().equals("Zomin")) { //39.88553183671697, 68.3643022291486
                            sendMessage(user.getChatId().toString(), setApi(39.88553183671697, 68.3643022291486, "Jizzah, Zomin"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_buxoro)) {
                        if (user.getData().equals("Buxoro shahri")) {//39.77672367865153, 64.4210785732946
                            sendMessage(user.getChatId().toString(), setApi(39.77672367865153, 64.4210785732946, "Buxoro, Buxoro shahri"));
                        } else if (user.getData().equals("Buxoro tumani")) { //39.63566090876281, 64.30230294221505
                            sendMessage(user.getChatId().toString(), setApi(39.63566090876281, 64.30230294221505, "Buxoro, Buxoro tumani"));
                        } else if (user.getData().equals("G'ijduvon")) {//40.601906240839995, 64.71053498588797
                            sendMessage(user.getChatId().toString(), setApi(40.601906240839995, 64.71053498588797, "Buxoro, G'ijduvon"));
                        } else if (user.getData().equals("Jondor")) {//39.971219891973426, 63.5714954269693
                            sendMessage(user.getChatId().toString(), setApi(39.971219891973426, 63.5714954269693, "Buxoro, Jondor"));
                        } else if (user.getData().equals("Kogon shahri")) { //39.72355224686954, 64.55132893267286
                            sendMessage(user.getChatId().toString(), setApi(39.72355224686954, 64.55132893267286, "Buxoro, Kogon shahri"));
                        } else if (user.getData().equals("Kogon tumani")) { //39.69809076622412, 64.53702710674044
                            sendMessage(user.getChatId().toString(), setApi(39.69809076622412, 64.53702710674040, "Buxoro, Kogon tumani"));
                        } else if (user.getData().equals("Olot")) { //39.281691000170674, 64.03768298578096
                            sendMessage(user.getChatId().toString(), setApi(39.281691000170674, 64.03768298578096, "Buxoro, Olot"));
                        } else if (user.getData().equals("Peshku")) {//40.73303621917936, 63.21959281775061
                            sendMessage(user.getChatId().toString(), setApi(40.73303621917936, 63.21959281775061, "Buxoro, Peshku"));
                        } else if (user.getData().equals("Qorako'l")) { //39.936970007976, 62.941145138407805
                            sendMessage(user.getChatId().toString(), setApi(39.936970007976, 62.941145138407805, "Buxoro, Qorako'l"));
                        } else if (user.getData().equals("Qorovulbozor")) {//39.46527348718642, 64.67070586334809
                            sendMessage(user.getChatId().toString(), setApi(39.46527348718642, 64.67070586334809, "Buxoro, Qorovulbozor"));
                        } else if (user.getData().equals("Romitan")) { //40.731071302974954, 62.530718928951856
                            sendMessage(user.getChatId().toString(), setApi(40.731071302974954, 62.530718928951856, "Buxoro, Romitan"));
                        } else if (user.getData().equals("Shofirkon")) { // 40.12540773474785, 64.48270729594604
                            sendMessage(user.getChatId().toString(), setApi(40.12540773474785, 64.48270729594604, "Buxoro, Shofirkon"));
                        } else if (user.getData().equals("Vobkent")) { //40.15547517789773, 64.53244365200904
                            sendMessage(user.getChatId().toString(), setApi(40.15547517789773, 64.53244365200904, "Buxoro, Vobkent"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_xorazm)) {
                        if (user.getData().equals("Bog'ot")) {//41.33744153309746, 60.87694940015311
                            sendMessage(user.getChatId().toString(), setApi(41.33744153309746, 60.87694940015311, "Xorazm, Bog'ot"));
                        } else if (user.getData().equals("Gurlan")) {//41.90536583933732, 60.37982747138814
                            sendMessage(user.getChatId().toString(), setApi(41.90536583933732, 60.37982747138814, "Xorazm, Gurlan"));
                        } else if (user.getData().equals("Shovot")) {//41.710201238586826, 60.27012648475865
                            sendMessage(user.getChatId().toString(), setApi(41.710201238586826, 0.27012648475865, "Xorazm, Shovot"));
                        } else if (user.getData().equals("Qo'shko'pir")) {//41.526982196908484, 60.269577660413084
                            sendMessage(user.getChatId().toString(), setApi(41.526982196908484, 60.269577660413084, "Xorazm, Qo'shko'pir"));
                        } else if (user.getData().equals("Yangibozor")) {// 41.75025881608148, 60.54049313655263
                            sendMessage(user.getChatId().toString(), setApi(41.75025881608148, 60.54049313655263, "Xorazm, Yangibozor"));
                        } else if (user.getData().equals("Urganch shahri")) {//41.547992018041974, 60.618264246685676
                            sendMessage(user.getChatId().toString(), setApi(41.547992018041974, 60.618264246685676, "Xorazm, Urganch shahri"));
                        } else if (user.getData().equals("Urganch tumani")) {//41.61625437893366, 60.533593007029786
                            sendMessage(user.getChatId().toString(), setApi(41.61625437893366, 60.533593007029786, "Xorazm, Urganch tumani"));
                        } else if (user.getData().equals("Xazorasp")) {//41.30448683950074, 61.09745692690238
                            sendMessage(user.getChatId().toString(), setApi(41.30448683950074, 61.09745692690238, "Xorazm, Xazorasp"));
                        } else if (user.getData().equals("Xiva")) {//41.39184570391674, 60.34725590732044
                            sendMessage(user.getChatId().toString(), setApi(41.39184570391674, 60.34725590732044, "Xorazm, Xiva"));
                        } else if (user.getData().equals("Xonqa")) {//41.481393767464844, 60.829570881655584
                            sendMessage(user.getChatId().toString(), setApi(41.481393767464844, 60.829570881655584, "Xorazm, Xonqa"));
                        } else if (user.getData().equals("Yangiariq")) {// 41.346206916399666, 60.54804614851794
                            sendMessage(user.getChatId().toString(), setApi(41.346206916399666, 60.54804614851794, "Xorazm, Yangiariq"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_navoiy)) {
                        if (user.getData().equals("Navoiy shahri")) {//40.10166507561083, 65.3575344176788
                            sendMessage(user.getChatId().toString(), setApi(40.10166507561083, 65.3575344176788, "Navoiy, Navoiy shahri"));
                        } else if (user.getData().equals("Karmana")) {//40.14372322026956, 65.35467198347476
                            sendMessage(user.getChatId().toString(), setApi(40.14372322026956, 65.35467198347476, "Navoiy, Karmana"));
                        } else if (user.getData().equals("Konimex")) {// 40.277684998973186, 65.14210146382663
                            sendMessage(user.getChatId().toString(), setApi(40.277684998973186, 65.14210146382663, "Navoiy, Konimex"));
                        } else if (user.getData().equals("Navbahor")) {//40.232621293600054, 65.23269831157204
                            sendMessage(user.getChatId().toString(), setApi(40.232621293600054, 65.23269831157204, "Navoiy, Navbahor"));
                        } else if (user.getData().equals("Nurota")) {//40.61096195334514, 65.93817362794783
                            sendMessage(user.getChatId().toString(), setApi(40.61096195334514, 65.93817362794783, "Navoiy, Nurota"));
                        } else if (user.getData().equals("Qiziltepa")) {//39.8966432527515, 64.76528114807608
                            sendMessage(user.getChatId().toString(), setApi(39.8966432527515, 64.76528114807608, "Navoiy, Qiziltepa"));
                        } else if (user.getData().equals("Tomdi")) {//42.29906147558487, 64.90150727134733
                            sendMessage(user.getChatId().toString(), setApi(42.29906147558487, 64.90150727134733, "Navoiy, Tomdi"));
                        } else if (user.getData().equals("Uchquduq")) {//42.44642264030942, 62.6945789709221
                            sendMessage(user.getChatId().toString(), setApi(42.44642264030942, 62.6945789709221, "Navoiy, Uchquduq"));
                        } else if (user.getData().equals("Xatirchi")) {//40.230069075732, 65.9409253080852
                            sendMessage(user.getChatId().toString(), setApi(40.230069075732, 65.9409253080852, "Navoiy, Xatirchi"));
                        } else if (user.getData().equals("Zarafshon")) {//41.574460147847816, 64.18331570807477
                            sendMessage(user.getChatId().toString(), setApi(41.574460147847816, 64.18331570807477, "Navoiy, Zarafshon"));
                        }
                    } else if (user.getStep().equals(UserStep.select_lang_lt_qashqadaryo)) {
                        if (user.getData().equals("Qarshi shahri")) {//38.86191354410989, 65.78491933285783
                            sendMessage(user.getChatId().toString(), setApi(38.86191354410989, 65.78491933285783, "Qashqadaryo, Qarshi shahri"));
                        } else if (user.getData().equals("Qarshi tumani")) {//38.779267614177556, 65.75278333536036
                            sendMessage(user.getChatId().toString(), setApi(38.779267614177556, 65.75278333536036, "Qashqadaryo, Qarshi tumani"));
                        } else if (user.getData().equals("Chiroqchi")) {//39.18112111196543, 66.25891591391694
                            sendMessage(user.getChatId().toString(), setApi(39.18112111196543, 66.25891591391694, "Qashqadaryo, Chiroqchi"));
                        } else if (user.getData().equals("Dehqonobod")) {//38.35730703486065, 66.47533103897435
                            sendMessage(user.getChatId().toString(), setApi(38.35730703486065, 66.47533103897435, "Qashqadaryo, Dehqonobod"));
                        } else if (user.getData().equals("G'uzor")) {//38.587010422831746, 66.04489530163235
                            sendMessage(user.getChatId().toString(), setApi(38.587010422831746, 66.04489530163235, "Qashqadaryo, G'uzor"));
                        } else if (user.getData().equals("Kasbi")) {//38.93540618287719, 65.43383657129233
                            sendMessage(user.getChatId().toString(), setApi(38.93540618287719, 65.43383657129233, "Qashqadaryo, KasbiKasbi"));
                        } else if (user.getData().equals("Kitob")) {// 39.2152414606226, 67.03598426370185
                            sendMessage(user.getChatId().toString(), setApi(39.2152414606226, 67.03598426370185, "Qashqadaryo, Kitob"));
                        } else if (user.getData().equals("Koson")) {// 39.16770611275984, 65.77183802497865
                            sendMessage(user.getChatId().toString(), setApi(39.16770611275984, 65.77183802497865, "Qashqadaryo, Koson"));
                        } else if (user.getData().equals("Mirishkor")) {// 38.874194821533756, 64.92111490671182
                            sendMessage(user.getChatId().toString(), setApi(38.874194821533756, 64.92111490671182, "Qashqadaryo, Mirishkor"));
                        } else if (user.getData().equals("Muborak")) {//39.30420398425692, 65.25997593377225
                            sendMessage(user.getChatId().toString(), setApi(39.30420398425692, 65.25997593377225, "Qashqadaryo, Muborak"));
                        } else if (user.getData().equals("Nishon")) {//38.53608390327857, 65.48400423357076
                            sendMessage(user.getChatId().toString(), setApi(38.53608390327857, 65.48400423357076, "Qashqadaryo, Nishon"));
                        } else if (user.getData().equals("Qamashi")) {// 38.75660026948466, 66.60185133772104
                            sendMessage(user.getChatId().toString(), setApi(38.75660026948466, 66.60185133772104, "Qashqadaryo, Qamashi"));
                        } else if (user.getData().equals("Shahrisabz")) {//38.99171024720661, 67.2009187750465
                            sendMessage(user.getChatId().toString(), setApi(38.99171024720661, 67.2009187750465, "Qashqadaryo, Shahrisabz"));
                        } else if (user.getData().equals("Yakkabog'")) {//38.90742740574835, 66.75665915180723
                            sendMessage(user.getChatId().toString(), setApi(38.90742740574835, 66.75665915180723, "Qashqadaryo, Yakkabog'"));
                        }
                    } else if (user.getData().equals("location")) {
                        sendMessage(user.getChatId().toString(), setApi(user.getLat(), user.getLang(), "Lakatsiya bo'yicha"));
                        userService.updateLatLang(user, user.getId(), user.getLat(), user.getLang());
                    }
                    deleteInlineKeyboard(message);
                } else if (text.startsWith("??? ?????????????? ??a?????? ????????????????")) {
                    userService.updateOxirgiIsh(user, user.getId(), "??? ?????????????? ??a?????? ????????????????");
                    if (user.getStep().equals(UserStep.select_lang_krl_location)){
                        sendMessage(user.getChatId().toString() , setApi(user.getLat() , user.getLang() , "?????? ???????????? ??????"));
                    }
                  else if (user.getData().equals("Andijon shahri")) {//40.7865585435806, 72.31208083702941
                        sendMessage(user.getChatId().toString(), setApiK(40.7865585435806, 72.31208083702941, "A????????????, A???????????? ??????????"));
                    } else if (user.getData().equals("Xonobod")) { //40.79989825348597, 72.98951560333182
                        sendMessage(user.getChatId().toString(), setApiK(40.454055968860445, 72.98951560333182, "A????????????, ??????????????"));
                    } else if (user.getData().equals("Asaka")) {// 40.6619355989725, 72.24428848141633
                        sendMessage(user.getChatId().toString(), setApiK(40.6619355989725, 72.24428848141633, "A????????????, A????????"));
                    } else if (user.getData().equals("Baliqchi")) {//40.8681067477643, 71.90901428107483
                        sendMessage(user.getChatId().toString(), setApiK(40.8681067477643, 71.90901428107483, "A????????????, ??????????????"));
                    } else if (user.getData().equals("Bo'z")) { //40.67522368198782, 71.90202999274003
                        sendMessage(user.getChatId().toString(), setApiK(40.67522368198782, 71.90202999274003, "A????????????, ??????"));
                    } else if (user.getData().equals("Buloqboshi")) { //40.61824097722169, 72.4589485266868
                        sendMessage(user.getChatId().toString(), setApiK(40.61824097722169, 72.4589485266868, "A????????????, ??????????????????"));
                    } else if (user.getData().equals("Izboskan")) { //40.926914438296166, 72.23046114826215
                        sendMessage(user.getChatId().toString(), setApiK(40.926914438296166, 72.23046114826215, "A????????????, ????????????????"));
                    } else if (user.getData().equals("Jalolquduq")) {//40.73260859991856, 72.61228519306135
                        sendMessage(user.getChatId().toString(), setApiK(40.73260859991856, 72.61228519306135, "A????????????, ????????????????????"));
                    } else if (user.getData().equals("Marhamat")) { //40.51393917604782, 72.29981218880005
                        sendMessage(user.getChatId().toString(), setApiK(40.51393917604782, 72.29981218880005, "A????????????, ????????????????"));
                    } else if (user.getData().equals("Oltinko'l")) {// 40.78257767662204, 72.15630346953014
                        sendMessage(user.getChatId().toString(), setApiK(40.78257767662204, 72.15630346953014, "A????????????, ????????????????"));
                    } else if (user.getData().equals("Paxtaobod")) { //40.977855409832266, 72.42257164080198
                        sendMessage(user.getChatId().toString(), setApiK(40.977855409832266, 72.42257164080198, "A????????????, ??????????????????"));
                    } else if (user.getData().equals("Qo'rg'ontepa")) { // 40.737458816612616, 72.80489172467401
                        sendMessage(user.getChatId().toString(), setApiK(40.737458816612616, 72.80489172467401, "A????????????, ????????????????????"));
                    } else if (user.getData().equals("Ulug'nor")) { //40.7812938834895, 71.65829940832162
                        sendMessage(user.getChatId().toString(), setApiK(40.7812938834895, 71.65829940832162, "A????????????, ??????????????"));
                    } else if (user.getData().equals("Xo'jaobod")) { // 40.644265790848365, 72.59695277036937
                        sendMessage(user.getChatId().toString(), setApiK(40.644265790848365, 72.59695277036937, "A????????????, ????????????????"));
                    } else if (user.getData().equals("Shahrixon")) { //40.705658566969085, 72.04954603823948
                        sendMessage(user.getChatId().toString(), setApiK(40.705658566969085, 72.04954603823948, "A????????????, ????????????????"));
                    } else if (user.getData().equals("ToshknetSH")) { // 41.31862648082324, 69.26185247247427
                        sendMessage(user.getChatId().toString(), setApiK(41.31862648082324, 69.26185247247427, "?????????????? ,?????????????? ??????????"));
                    } else if (user.getData().equals("chilonzor")) { // 41.25966068034852, 69.17981683014095
                        sendMessage(user.getChatId().toString(), setApiK(41.25966068034852, 69.17981683014095, "?????????????? ,????????????????"));
                    } else if (user.getData().equals("quyiC")) { // 41.324249737732416, 69.27336159543128
                        sendMessage(user.getChatId().toString(), setApiK(41.324249737732416, 69.27336159543128, "?????????????? ,???????? ????????????"));
                    } else if (user.getData().equals("bostonliq")) { // 41.29962474317497, 69.24013777030345
                        sendMessage(user.getChatId().toString(), setApiK(41.29962474317497, 69.24013777030345, "?????????????? ,??????????????????"));
                    } else if (user.getData().equals("yangiyol")) { // 41.106601409336605, 69.00099564918969
                        sendMessage(user.getChatId().toString(), setApiK(41.106601409336605, 69.00099564918969, "?????????????? ,??????????????"));
                    } else if (user.getData().equals("zangota")) { // 41.19119250475624, 69.14481581613583
                        sendMessage(user.getChatId().toString(), setApiK(41.19119250475624, 69.14481581613583, "?????????????? ,????????????????"));
                    } else if (user.getData().equals("ohangaron")) { // 40.91797520041142, 69.63525266877876
                        sendMessage(user.getChatId().toString(), setApiK(40.91797520041142, 69.63525266877876, "?????????????? ,??????????????????"));
                    } else if (user.getData().equals("oqqq")) { // 40.88044288436734, 69.04266437650311
                        sendMessage(user.getChatId().toString(), setApiK(40.88044288436734, 69.04266437650311, "?????????????? ,????????????????"));
                    } else if (user.getData().equals("ppp")) { // 41.297497902393545, 69.68451728729993
                        sendMessage(user.getChatId().toString(), setApiK(41.297497902393545, 69.68451728729993, "?????????????? ,????????????????"));
                    } else if (user.getData().equals("ortachirchiq")) { // 41.20590993388904, 69.27922461238849
                        sendMessage(user.getChatId().toString(), setApiK(41.20590993388904, 69.27922461238849, "?????????????? ,???????? ????????????"));
                    } else if (user.getData().equals("yuqorichirchiq")) { // 41.20911424680261, 69.49407488275496
                        sendMessage(user.getChatId().toString(), setApiK(41.20911424680261, 69.49407488275496, "?????????????? ,?????????? ????????????"));
                    } else if (user.getData().equals("bekobod")) { // 40.454055968860445, 69.1819780480247
                        sendMessage(user.getChatId().toString(), setApiK(40.454055968860445, 69.1819780480247, "?????????????? ,??????????????"));
                    } else if (user.getData().equals("Samarqand shahri")) {//39.65329327101519, 66.95826234725476
                        sendMessage(user.getChatId().toString(), setApiK(39.65329327101519, 66.95826234725476, "??????????????????, ?????????????????? ??????????"));
                    } else if (user.getData().equals("Bulung'ur")) {//39.70791766177822, 67.28142857964608
                        sendMessage(user.getChatId().toString(), setApiK(39.70791766177822, 67.28142857964608, "??????????????????, ????????????????"));
                    } else if (user.getData().equals("Ishtixon")) {//39.98829349793204, 66.53144028860048
                        sendMessage(user.getChatId().toString(), setApiK(39.98829349793204, 66.53144028860048, "??????????????????, ??????????????"));
                    } else if (user.getData().equals("Jomboy")) {//39.76625238583371, 67.07779535428304
                        sendMessage(user.getChatId().toString(), setApiK(39.76625238583371, 67.07779535428304, "??????????????????, ????????????"));
                    } else if (user.getData().equals("Kattaqo'rg'on tumani")) { // 39.981981100516265, 66.2896029199572
                        sendMessage(user.getChatId().toString(), setApiK(39.981981100516265, 66.2896029199572, "??????????????????, ?????????????????????? ??????????"));
                    } else if (user.getData().equals("Kattaqo'rg'on shahri")) { // 39.8993149735457, 66.2633149577346
                        sendMessage(user.getChatId().toString(), setApiK(39.8993149735457, 66.2633149577346, "??????????????????, ?????????????????????? ????????????"));
                    } else if (user.getData().equals("Narpay")) {//39.92524679769102, 65.98627826494945
                        sendMessage(user.getChatId().toString(), setApiK(39.92524679769102, 65.98627826494945, "??????????????????, ????????????"));
                    } else if (user.getData().equals("Nurobod")) {//39.57127098105206, 66.02799000748902
                        sendMessage(user.getChatId().toString(), setApiK(39.57127098105206, 66.02799000748902, "??????????????????, ??????????????"));
                    } else if (user.getData().equals("Oqdaryo")) {//39.839873784601465, 66.73834694617673
                        sendMessage(user.getChatId().toString(), setApiK(39.839873784601465, 66.73834694617673, "??????????????????, ????????????"));
                    } else if (user.getData().equals("Past darg'om")) { // 39.704352292734065, 66.62242053041332
                        sendMessage(user.getChatId().toString(), setApiK(39.704352292734065, 66.62242053041332, "??????????????????, ???????? ????????????"));
                    } else if (user.getData().equals("Urgut")) {//39.447002526177904, 67.17173665735707
                        sendMessage(user.getChatId().toString(), setApiK(39.447002526177904, 67.17173665735707, "??????????????????, ??????????"));
                    } else if (user.getData().equals("Paxtachi")) {//39.888515759299004, 65.6089318735053
                        sendMessage(user.getChatId().toString(), setApiK(39.888515759299004, 65.6089318735053, "??????????????????, ??????????????"));
                    } else if (user.getData().equals("Poyariq")) {//40.05104556455984, 66.85419779064077
                        sendMessage(user.getChatId().toString(), setApiK(40.05104556455984, 66.85419779064077, "??????????????????, ????????????"));
                    } else if (user.getData().equals("Samarqand tumani")) { //39.59818617036431, 66.90471762188967
                        sendMessage(user.getChatId().toString(), setApiK(39.59818617036431, 66.90471762188967, "??????????????????, ?????????????????? ????????????"));
                    } else if (user.getData().equals("Toyloq")) { //39.56442359774873, 67.14018598905683
                        sendMessage(user.getChatId().toString(), setApiK(39.56442359774873, 67.14018598905683, "??????????????????, ????????????"));
                    } else if (user.getData().equals("Qo'shrabot")) { //40.38655726843597, 66.5100175021684
                        sendMessage(user.getChatId().toString(), setApiK(40.38655726843597, 66.5100175021684, "??????????????????, ????????????????"));
                    } else if (user.getData().equals("Buxoro shahri")) {//39.77672367865153, 64.4210785732946
                        sendMessage(user.getChatId().toString(), setApiK(39.77672367865153, 64.4210785732946, "????????????, ???????????? ??????????"));
                    } else if (user.getData().equals("Buxoro tumani")) { //39.63566090876281, 64.30230294221505
                        sendMessage(user.getChatId().toString(), setApiK(39.63566090876281, 64.30230294221505, "????????????, ???????????? ????????????"));
                    } else if (user.getData().equals("G'ijduvon")) {//40.601906240839995, 64.71053498588797
                        sendMessage(user.getChatId().toString(), setApiK(40.601906240839995, 64.71053498588797, "????????????, ????????????????"));
                    } else if (user.getData().equals("Jondor")) {//39.971219891973426, 63.5714954269693
                        sendMessage(user.getChatId().toString(), setApiK(39.971219891973426, 63.5714954269693, "????????????, ????????????"));
                    } else if (user.getData().equals("Kogon shahri")) { //39.72355224686954, 64.55132893267286
                        sendMessage(user.getChatId().toString(), setApiK(39.72355224686954, 64.55132893267286, "????????????, ?????????? ??????????"));
                    } else if (user.getData().equals("Kogon tumani")) { //39.69809076622412, 64.53702710674044
                        sendMessage(user.getChatId().toString(), setApiK(39.69809076622412, 64.53702710674044, "????????????, ?????????? ????????????"));
                    } else if (user.getData().equals("Olot")) { //39.281691000170674, 64.03768298578096
                        sendMessage(user.getChatId().toString(), setApiK(39.281691000170674, 64.03768298578096, "????????????, ????????"));
                    } else if (user.getData().equals("Peshku")) {//40.73303621917936, 63.21959281775061
                        sendMessage(user.getChatId().toString(), setApiK(40.73303621917936, 63.21959281775061, "????????????, ??????????"));
                    } else if (user.getData().equals("Qorako'l")) { //39.936970007976, 62.941145138407805
                        sendMessage(user.getChatId().toString(), setApiK(39.936970007976, 62.941145138407805, "????????????, ??????????????"));
                    } else if (user.getData().equals("Qorovulbozor")) {//39.46527348718642, 64.67070586334809
                        sendMessage(user.getChatId().toString(), setApiK(39.46527348718642, 64.67070586334809, "????????????, ????????????????????????"));
                    } else if (user.getData().equals("Romitan")) { //40.731071302974954, 62.530718928951856
                        sendMessage(user.getChatId().toString(), setApiK(40.731071302974954, 62.530718928951856, "????????????, ??????????????"));
                    } else if (user.getData().equals("Shofirkon")) { // 40.12540773474785, 64.48270729594604
                        sendMessage(user.getChatId().toString(), setApiK(40.12540773474785, 64.48270729594604, "????????????, ????????????????"));
                    } else if (user.getData().equals("Vobkent")) { //40.15547517789773, 64.53244365200904
                        sendMessage(user.getChatId().toString(), setApiK(40.15547517789773, 64.53244365200904, "????????????, ??????????????"));
                    } else if (user.getData().equals("Karmana")) {//40.14372322026956, 65.35467198347476
                        sendMessage(user.getChatId().toString(), setApiK(40.14372322026956, 65.35467198347476, "????????????, ??????????????"));
                    } else if (user.getData().equals("Konimex")) {// 40.277684998973186, 65.14210146382663
                        sendMessage(user.getChatId().toString(), setApiK(40.277684998973186, 65.14210146382663, "????????????, ??????????????"));
                    } else if (user.getData().equals("Navbahor")) {//40.232621293600054, 65.23269831157204
                        sendMessage(user.getChatId().toString(), setApiK(40.232621293600054, 65.23269831157204, "????????????, ????????????????"));
                    } else if (user.getData().equals("Nurota")) {//40.61096195334514, 65.93817362794783
                        sendMessage(user.getChatId().toString(), setApiK(40.61096195334514, 65.93817362794783, "????????????, ????????????"));
                    } else if (user.getData().equals("Qiziltepa")) {//39.8966432527515, 64.76528114807608
                        sendMessage(user.getChatId().toString(), setApiK(39.8966432527515, 64.76528114807608, "????????????, ??????????????????"));
                    } else if (user.getData().equals("Tomdi")) {//42.29906147558487, 64.90150727134733
                        sendMessage(user.getChatId().toString(), setApiK(42.29906147558487, 64.90150727134733, "????????????, ??????????"));
                    } else if (user.getData().equals("Uchquduq")) {//42.44642264030942, 62.6945789709221
                        sendMessage(user.getChatId().toString(), setApiK(42.44642264030942, 62.6945789709221, "????????????, ??????????????"));
                    } else if (user.getData().equals("Xatirchi")) {//40.230069075732, 65.9409253080852
                        sendMessage(user.getChatId().toString(), setApiK(40.230069075732, 65.9409253080852, "????????????, ??????????????"));
                    } else if (user.getData().equals("Zarafshon")) {//41.574460147847816, 64.18331570807477
                        sendMessage(user.getChatId().toString(), setApiK(41.574460147847816, 64.18331570807477, "????????????, ????????????????"));
                    } else if (user.getData().equals("Chust")) { // 41.00615643758215, 71.23771796120751
                        sendMessage(user.getChatId().toString(), setApiK(40.999931, 71.236137, "????????????????, ????????"));
//                        buttongaVatBnJonatish(user.getChatId(), 40.999931 , 71.236137 , "????????????????, ????????");
                    } else if (user.getData().equals("Namangan")) { // Namangan shahar
                        sendMessage(user.getChatId().toString(), setApiK(41.007162, 71.643712, "????????????????, ???????????????? ??????????"));
                    } else if (user.getData().equals("Kosonsoy")) { // Kosonsoy shahar
                        sendMessage(user.getChatId().toString(), setApiK(41.19608642817571, 71.52652970907708, "????????????????, ????????????????"));
                    } else if (user.getData().equals("Chortoq")) { // Chortoq shahar 41.06368431981446, 71.8230565658223
                        sendMessage(user.getChatId().toString(), setApiK(41.06368431981446, 71.8230565658223, "????????????????, ????????????"));
                    } else if (user.getData().equals("Norin")) { // Namangan shahar 40.940509509996495, 72.00654567901067
                        sendMessage(user.getChatId().toString(), setApiK(40.940509509996495, 72.00654567901067, "????????????????, ??????????"));
                    } else if (user.getData().equals("Mingbuloq")) { // Namangan shahar 40.78917645308457, 71.38875951462421
                        sendMessage(user.getChatId().toString(), setApiK(40.78917645308457, 71.38875951462421, "????????????????, ??????????????????"));
                    } else if (user.getData().equals("Pop")) { // Namangan shahar 41.0364117324324, 70.75487013756214
                        sendMessage(user.getChatId().toString(), setApiK(41.0364117324324, 70.75487013756214, "????????????????, ??????"));
                    } else if (user.getData().equals("To'raqo'rg'on")) { // Namangan shahar 40.99316007632018, 71.51626227933346
                        sendMessage(user.getChatId().toString(), setApiK(40.99316007632018, 71.51626227933346, "????????????????, ????????????????????"));
                    } else if (user.getData().equals("Uchqo'rg'on")) { // Namangan shahar 41.12068842248576, 72.08711447153537
                        sendMessage(user.getChatId().toString(), setApiK(41.12068842248576, 72.08711447153537, "????????????????, ????????????????"));
                    } else if (user.getData().equals("uychi")) { // Namangan shahar 41.06721589284562, 71.92292974977094
                        sendMessage(user.getChatId().toString(), setApiK(71.92292974977094, 71.92292974977094, "????????????????, ????????"));
                    } else if (user.getData().equals("yangiqorgon")) { // Namangan shahar 41.198968289144275, 71.71703917988339
                        sendMessage(user.getChatId().toString(), setApiK(41.198968289144275, 71.71703917988339, "????????????????, ????????????????????"));
                    } else if (user.getData().equals("Farg???ona shahar")) { //40.37949214005235, 71.78153222147638
                        sendMessage(user.getChatId().toString(), setApiK(40.37949214005235, 71.78153222147638, "??????????????, ?????????????? ??????????"));
                    } else if (user.getData().equals("Uchko???prik")) { //40.53267491097347, 71.02892566146784
                        sendMessage(user.getChatId().toString(), setApiK(40.53267491097347, 71.02892566146784, "??????????????, ????????????????"));
                    } else if (user.getData().equals("So???x")) { //39.968927659480165, 71.12908694248515
                        sendMessage(user.getChatId().toString(), setApiK(39.968927659480165, 71.12908694248515, "??????????????, ??????"));
                    } else if (user.getData().equals("Yozyovon")) { //40.62131813678478, 71.6480687836224
                        sendMessage(user.getChatId().toString(), setApiK(40.62131813678478, 71.6480687836224, "??????????????, ????????????"));
                    } else if (user.getData().equals("Quva")) { //40.524573367810504, 72.06257627873157
                        sendMessage(user.getChatId().toString(), setApiK(40.524573367810504, 72.06257627873157, "??????????????, ????????"));
                    } else if (user.getData().equals("Qo???qon")) { //40.524621966621304, 70.93356479162965
                        sendMessage(user.getChatId().toString(), setApiK(40.524621966621304, 70.93356479162965, "??????????????, ??????????"));
                    } else if (user.getData().equals("Beshariq")) { //40.39690886265036, 70.57359975866977
                        sendMessage(user.getChatId().toString(), setApiK(40.39690886265036, 70.5735997586697, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Quvasoy")) { //
                        sendMessage(user.getChatId().toString(), setApiK(40.30222, 71.97444, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Farg???ona")) { //40.320714145540286, 71.70209565835899
                        sendMessage(user.getChatId().toString(), setApiK(40.320714145540286, 71.70209565835899, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Toshloq")) { //40.56269866588281, 71.84044642257277
                        sendMessage(user.getChatId().toString(), setApiK(40.56269866588281, 71.84044642257277, "??????????????, ????????????"));
                    } else if (user.getData().equals("O???zbekiston")) { //40.330297796679396, 70.85868054864669
                        sendMessage(user.getChatId().toString(), setApiK(40.330297796679396, 70.85868054864669, "??????????????, ????????????????????"));
                    } else if (user.getData().equals("Qo???shtepa")) { //40.53692345413236, 71.6415020647065
                        sendMessage(user.getChatId().toString(), setApiK(40.53692345413236, 71.6415020647065, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Marg???ilon")) { //40.47635685369382, 71.71252435127899
                        sendMessage(user.getChatId().toString(), setApiK(40.47635685369382, 71.71252435127899, "??????????????, ????????????????"));
                    } else if (user.getData().equals("Buvayda")) { //40.645693110093404, 71.10204505839337
                        sendMessage(user.getChatId().toString(), setApiK(40.645693110093404, 71.10204505839337, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Dang???ara")) { //40.58139710281787, 70.91477380795894
                        sendMessage(user.getChatId().toString(), setApiK(40.58139710281787, 70.91477380795894, "??????????????, ??????????????"));
                    } else if (user.getData().equals("Oltiariq")) { //40.39370084174377, 71.48670380912914
                        sendMessage(user.getChatId().toString(), setApiK(40.39370084174377, 71.48670380912914, "??????????????, ????????????????"));
                    } else if (user.getData().equals("Rishton")) { //40.36410679785719, 71.27208639645364
                        sendMessage(user.getChatId().toString(), setApiK(40.36410679785719, 71.27208639645364, "??????????????, ????????????"));
                    } else if (user.getData().equals("Furqat")) { //40.51673390388502, 70.71177286893743
                        sendMessage(user.getChatId().toString(), setApiK(40.51673390388502, 70.71177286893743, "??????????????, ????????????"));
                    } else if (user.getData().equals("Bag???dod")) { //40.49387968871484, 71.20366862141532
                        sendMessage(user.getChatId().toString(), setApiK(40.49387968871484, 71.20366862141532, "??????????????, ????????????"));
                    } else if (user.getData().equals("Jizzax shahri")) {//40.11977007458388, 67.85232343283624
                        sendMessage(user.getChatId().toString(), setApiK(40.11977007458388, 67.85232343283624, "????????????, ???????????? ??????????"));
                    } else if (user.getData().equals("Arnasoy")) { //40.59901232957254, 67.79358090175431
                        sendMessage(user.getChatId().toString(), setApiK(40.59901232957254, 67.7935809017431, "????????????, A????????????"));
                    } else if (user.getData().equals("Baxmal")) {//39.78225382213002, 67.7250016175458
                        sendMessage(user.getChatId().toString(), setApiK(39.78225382213002, 67.7250016175458, "????????????, ????????????"));
                    } else if (user.getData().equals("Forish")) {//40.70901562393393, 67.18181520202268
                        sendMessage(user.getChatId().toString(), setApiK(40.70901562393393, 67.18181520202268, "????????????, ??????????"));
                    } else if (user.getData().equals("G'allaorol")) { //40.10205495202923, 67.32846413281408
                        sendMessage(user.getChatId().toString(), setApiK(40.10205495202923, 67.32846413281408, "????????????, ??????????????????"));
                    } else if (user.getData().equals("Do'stlik")) { //40.58181976770775, 68.04548305263552
                        sendMessage(user.getChatId().toString(), setApiK(40.58181976770775, 68.04548305263552, "????????????, ??????????????"));
                    } else if (user.getData().equals("Jizzax tumani")) { //40.11455358141378, 67.82473708142044
                        sendMessage(user.getChatId().toString(), setApiK(40.11455358141378, 67.82473708142044, "????????????, ???????????? ????????????"));
                    } else if (user.getData().equals("Mirzacho'l")) {//40.724412215707, 68.06118184566003
                        sendMessage(user.getChatId().toString(), setApiK(40.724412215707, 68.06118184566003, "????????????, ????????????????"));
                    } else if (user.getData().equals("Paxtakor")) { //40.35950221136383, 68.00475878330718
                        sendMessage(user.getChatId().toString(), setApiK(40.35950221136383, 68.00475878330718, "????????????, ????????????????"));
                    } else if (user.getData().equals("Yangiobod")) {//39.99243484998208, 68.74527110184545
                        sendMessage(user.getChatId().toString(), setApiK(39.99243484998208, 68.74527110184545, "????????????, ????????????????"));
                    } else if (user.getData().equals("Zafarobod")) { //40.41330581212753, 67.76451866402623
                        sendMessage(user.getChatId().toString(), setApiK(40.41330581212753, 67.76451866402623, "????????????, ??????????????????"));
                    } else if (user.getData().equals("Zarband")) { //40.189983986463055, 68.11455808514548
                        sendMessage(user.getChatId().toString(), setApiK(40.189983986463055, 68.11455808514548, "????????????, ??????????????"));
                    } else if (user.getData().equals("Zomin")) { //39.88553183671697, 68.3643022291486
                        sendMessage(user.getChatId().toString(), setApiK(39.88553183671697, 68.3643022291486, "????????????, ??????????"));
                    } else if (user.getData().equals("Bog'ot")) {//41.33744153309746, 60.87694940015311
                        sendMessage(user.getChatId().toString(), setApiK(41.33744153309746, 60.87694940015311, "????????????, ??????????"));
                    } else if (user.getData().equals("Gurlan")) {//41.90536583933732, 60.37982747138814
                        sendMessage(user.getChatId().toString(), setApiK(41.90536583933732, 60.37982747138814, "????????????, ????????????"));
                    } else if (user.getData().equals("Shovot")) {//41.710201238586826, 60.27012648475865
                        sendMessage(user.getChatId().toString(), setApiK(41.710201238586826, 60.27012648475865, "????????????, ??????????"));
                    } else if (user.getData().equals("Qo'shko'pir")) {//41.526982196908484, 60.269577660413084
                        sendMessage(user.getChatId().toString(), setApiK(41.526982196908484, 60.269577660413084, "????????????, ???????????????? "));
                    } else if (user.getData().equals("Yangibozor")) {// 41.75025881608148, 60.54049313655263
                        sendMessage(user.getChatId().toString(), setApiK(41.75025881608148, 60.54049313655263, "????????????, ?????????????????? "));
                    } else if (user.getData().equals("Urganch shahri")) {//41.547992018041974, 60.618264246685676
                        sendMessage(user.getChatId().toString(), setApiK(41.547992018041974, 60.618264246685676, "????????????, ???????????? ??????????"));
                    } else if (user.getData().equals("Urganch tumani")) {//41.61625437893366, 60.533593007029786
                        sendMessage(user.getChatId().toString(), setApiK(41.61625437893366, 60.533593007029786, "????????????, ???????????? ????????????"));
                    } else if (user.getData().equals("Xazorasp")) {//41.30448683950074, 61.09745692690238
                        sendMessage(user.getChatId().toString(), setApiK(41.30448683950074, 61.09745692690238, "????????????, ???????????????? "));
                    } else if (user.getData().equals("Xiva")) {//41.39184570391674, 60.34725590732044
                        sendMessage(user.getChatId().toString(), setApiK(41.39184570391674, 60.34725590732044, "????????????, ???????? "));
                    } else if (user.getData().equals("Xonqa")) {//41.481393767464844, 60.829570881655584
                        sendMessage(user.getChatId().toString(), setApiK(41.481393767464844, 60.829570881655584, "????????????, ??????????"));
                    } else if (user.getData().equals("Yangiariq")) {// 41.346206916399666, 60.54804614851794
                        sendMessage(user.getChatId().toString(), setApiK(41.346206916399666, 60.54804614851794, "????????????, ????????????????"));
                    } else if (user.getData().equals("Qarshi shahri")) {//38.86191354410989, 65.78491933285783
                        sendMessage(user.getChatId().toString(), setApiK(38.86191354410989, 65.78491933285783, "??????????????????, ?????????? ?????????? "));
                    } else if (user.getData().equals("Qarshi tumani")) {//38.779267614177556, 65.75278333536036
                        sendMessage(user.getChatId().toString(), setApiK(38.779267614177556, 65.75278333536036, "??????????????????, ?????????? ????????????"));
                    } else if (user.getData().equals("Chiroqchi")) {//39.18112111196543, 66.25891591391694
                        sendMessage(user.getChatId().toString(), setApiK(39.18112111196543, 66.25891591391694, "??????????????????, ?????????????? "));
                    } else if (user.getData().equals("Dehqonobod")) {//38.35730703486065, 66.47533103897435
                        sendMessage(user.getChatId().toString(), setApiK(38.35730703486065, 66.47533103897435, "??????????????????, ????????????????????"));
                    } else if (user.getData().equals("G'uzor")) {//38.587010422831746, 66.04489530163235
                        sendMessage(user.getChatId().toString(), setApiK(38.587010422831746, 66.04489530163235, "??????????????????, ?????????? "));
                    } else if (user.getData().equals("Kasbi")) {//38.93540618287719, 65.43383657129233
                        sendMessage(user.getChatId().toString(), setApiK(38.93540618287719, 65.43383657129233, "??????????????????, ?????????? "));
                    } else if (user.getData().equals("Kitob")) {// 39.2152414606226, 67.03598426370185
                        sendMessage(user.getChatId().toString(), setApiK(39.2152414606226, 67.03598426370185, "??????????????????, ?????????? "));
                    } else if (user.getData().equals("Koson")) {// 39.16770611275984, 65.77183802497865
                        sendMessage(user.getChatId().toString(), setApiK(39.16770611275984, 65.77183802497865, "??????????????????, ?????????? "));
                    } else if (user.getData().equals("Mirishkor")) {// 38.874194821533756, 64.92111490671182
                        sendMessage(user.getChatId().toString(), setApiK(38.874194821533756, 64.92111490671182, "??????????????????, ???????????????? "));
                    } else if (user.getData().equals("Muborak")) {//39.30420398425692, 65.25997593377225
                        sendMessage(user.getChatId().toString(), setApiK(39.30420398425692, 65.25997593377225, "??????????????????, ?????????????? "));
                    } else if (user.getData().equals("Nishon")) {//38.53608390327857, 65.48400423357076
                        sendMessage(user.getChatId().toString(), setApiK(38.53608390327857, 65.48400423357076, "??????????????????, ?????????? "));
                    } else if (user.getData().equals("Qamashi")) {// 38.75660026948466, 66.60185133772104
                        sendMessage(user.getChatId().toString(), setApiK(38.75660026948466, 66.60185133772104, "??????????????????, ???????????? "));
                    } else if (user.getData().equals("Shahrisabz")) {//38.99171024720661, 67.2009187750465
                        sendMessage(user.getChatId().toString(), setApiK(38.99171024720661, 67.2009187750465, "??????????????????, ??????????????????"));
                    } else if (user.getData().equals("Yakkabog'")) {//38.90742740574835, 66.75665915180723
                        sendMessage(user.getChatId().toString(), setApiK(38.90742740574835, 66.75665915180723, "??????????????????, ??????????????"));
                    }
                    userService.updateData(user, user.getId(), user.getData());
                    deleteInlineKeyboard(message);
                }


//                else if (text.startsWith("Hudud")
//                        || text.startsWith("KRIL")
//                        || text.startsWith("??????????")
//                        || text.startsWith("??????????????")
//                        || text.startsWith("???")) {
//                    deleteInlineKeyboard(message);
//                }
            } else if (message.hasLocation() && user.getOxirgiIsh().equals("location_")) {
                replyRemove_1(user.getChatId().toString());
                double lat = message.getLocation().getLatitude();
                double lang = message.getLocation().getLongitude();
                userService.updateLatLang(user, user.getId(), lat, lang);
                deleteInlineKeyboard_1(message);
//                replyRemove(user.getChatId().toString());
                if (user.getStep().substring(12, 13).equals("k")) {
                    deleteInlineKeyboard1(message);
                    buttongaVatBnJonatishKrl(user.getChatId(), lat, lang, "?????? ???????????? ??????", "");
                    userService.update(user ,  user.getId() , UserStep.select_lang_krl_location);
                } else {
                    deleteInlineKeyboard1(message);
                    buttongaVatBnJonatish(user.getChatId(), lat,lang, "Siz turgan joy", "");
                    userService.update(user, user.getId(), UserStep.select_lang_lt_location);
                }
            }
        } else if (update.hasCallbackQuery()) {
            User user = userService.findByChatId(update.getCallbackQuery().getMessage().getChatId());
            String data = update.getCallbackQuery().getData();
            deleteInlineKeyboard(user.getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId().toString());
            userService.updateOxirgiIsh(user, user.getId(), "/null");
            if (user.getStep().equals(UserStep.select_lang)) {
                if (data.equals("lt")) {
                    getRegionInline(user.getChatId());
                    user = userService.update(user, user.getId(), UserStep.select_lang_lt);
                } else if (data.equals("krl")) {
                    sendInlineKeyBoardRegionK(user.getChatId());
                    user = userService.update(user, user.getId(), UserStep.select_lang_krl);
                }
            } else if (data.equals("location")
            && user.getStep().substring(12 , 13).equals("l")) {
                locationBTN(user.getChatId());
                userService.updateOxirgiIsh(user, user.getId(), "location_");
            }
            else if ( user.getStep().substring(12 , 13).equals("k") &&
                    data.equals("location")) {
                locationBTNK(user.getChatId());
                userService.updateOxirgiIsh(user, user.getId(), "location_");
            } else if (user.getStep().equals(UserStep.select_lang_lt)) {
                if (data.equals("namangan")) {
                    namanganTumans(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_namangan);
                } else if (data.equals("toshkent")) {
                    sendInlineToshknet(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_toshkent);
                } else if (data.equals("andijon")) {
                    andijonTumans(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_andijon);
                } else if (data.equals("fargona")) {
                    sendInlineFargona(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_fargona);
                } else if (data.equals("samarqand")) {
                    sendInlineSamarqand(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_samarqand);
                } else if (data.equals("jizzah")) {
                    sendInlineJizzah(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_jizzah);
                } else if (data.equals("buxoro")) {
                    sendInlineBuxoro(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_lt_buxoro);
                } else if (data.equals("xorazm")) {
                    sendInlineXorazm(user.getChatId());
                    userService.update(user, user.getChatId(), UserStep.select_lang_lt_xorazm);
                } else if (data.equals("navoiy")) {
                    sendInlineNavoiy(user.getChatId());
                    userService.update(user, user.getChatId(), UserStep.select_lang_lt_navoiy);
                } else if (data.equals("qashqadaryo")) {
                    sendInlineQashqadaryo(user.getChatId());
                    userService.update(user, user.getChatId(), UserStep.select_lang_lt_qashqadaryo);
                } else if (data.equals("location")) {
                    sendMessage(user.getChatId().toString(), "Iltimos o'zingiz joylashgan joy lakatsiyasini botga tashlang...");
                    userService.update(user, user.getId(), UserStep.select_lang_lt_location);
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_namangan)) {
                if (data.equals("Chust")) { // 41.00615643758215, 71.23771796120751
//                    sendMessage(user.getChatId().toString(), setApi(40.999931, 71.236137, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 40.999931, 71.236137, "Namangan, Chust", "Chust");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Namangan")) { // Namangan shahar
//                    sendMessage(user.getChatId().toString(), setApi(41.007162, 71.643712, "Namangan, Namangan shahar"));
                    buttongaVatBnJonatish(user.getChatId(), 41.007162, 71.643712, "Namangan, Namangan shahar", "Namangan");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Kosonsoy")) { // Kosonsoy shahar
//                    sendMessage(user.getChatId().toString(), setApi(41.19608642817571, 71.52652970907708, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.19608642817571, 71.52652970907708, "Namangan, Kosonsoy", "Kosonsoy");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Chortoq")) { // Chortoq shahar 41.06368431981446, 71.8230565658223
//                    sendMessage(user.getChatId().toString(), setApi(41.06368431981446, 71.8230565658223, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.06368431981446, 71.8230565658223, "Namangan, Chortoq", "Chortoq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Norin")) { // Namangan shahar 40.940509509996495, 72.00654567901067
//                    sendMessage(user.getChatId().toString(), setApiK(40.940509509996495, 72.00654567901067, "????????????????, ??????????"));
                    buttongaVatBnJonatish(user.getChatId(), 40.940509509996495, 72.00654567901067, "Namangan, Norin", "Norin");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Mingbuloq")) { // Namangan shahar 40.78917645308457, 71.38875951462421
//                    sendMessage(user.getChatId().toString(), setApi(40.78917645308457, 71.38875951462421, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 40.78917645308457, 71.38875951462421, "Namangan, Mingbuloq", "Mingbuloq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Pop")) { // Namangan shahar 41.0364117324324, 70.75487013756214
//                    sendMessage(user.getChatId().toString(), setApi(41.0364117324324, 70.75487013756214, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.0364117324324, 70.75487013756214, "Namangan, Pop", "Pop");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("To'raqo'rg'on")) { // Namangan shahar 40.99316007632018, 71.51626227933346
//                    sendMessage(user.getChatId().toString(), setApi(40.99316007632018, 71.51626227933346, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 40.99316007632018, 71.51626227933346, "Namangan, To'raqo'rg'on", "To'raqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Uchqo'rg'on")) { // Namangan shahar 41.12068842248576, 72.08711447153537
//                    sendMessage(user.getChatId().toString(), setApi(41.12068842248576, 72.08711447153537, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.12068842248576, 72.08711447153537, "Namangan, Uchqo'rg'on", "Uchqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("uychi")) { // Namangan shahar 41.06721589284562, 71.92292974977094
//                    sendMessage(user.getChatId().toString(), setApi(71.92292974977094, 71.92292974977094, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.06721589284562, 71.92292974977094, "Namangan, Uychi", "Uychi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("yangiqorgon")) { // Namangan shahar 41.198968289144275, 71.71703917988339
//                    sendMessage(user.getChatId().toString(), setApi(41.198968289144275, 71.71703917988339, "Namangan, " + data));
                    buttongaVatBnJonatish(user.getChatId(), 41.198968289144275, 71.71703917988339, "Namangan, Yangiqo'rg'on", "Yangiqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_toshkent)) {
                if (data.equals("ToshknetSH")) { // 41.31862648082324, 69.26185247247427
//                    sendMessage(user.getChatId().toString(), setApi(41.31862648082324, 69.26185247247427, "Toshkent ,Toshkent Shahar"));
                    buttongaVatBnJonatish(user.getChatId(), 41.31862648082324, 69.26185247247427, "Toshkent ,Toshkent Shahar", "Toshkent");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("chilonzor")) { // 41.25966068034852, 69.17981683014095
//                    sendMessage(user.getChatId().toString(), setApi(41.25966068034852, 69.17981683014095, "Toshkent ,Chilonzor"));
                    buttongaVatBnJonatish(user.getChatId(), 41.25966068034852, 69.17981683014095, "Toshkent ,Chilonzor", "Chilonzor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("quyiC")) { // 41.324249737732416, 69.27336159543128
//                    sendMessage(user.getChatId().toString(), setApi(41.324249737732416, 69.27336159543128, "Toshkent ,Quyi Chirchiq"));
                    buttongaVatBnJonatish(user.getChatId(), 41.324249737732416, 69.27336159543128, "Toshkent ,Quyi Chirchiq", "Quyi Chirchiq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("bostonliq")) { // 41.29962474317497, 69.24013777030345
//                    sendMessage(user.getChatId().toString(), setApi(41.29962474317497, 69.24013777030345, "Toshkent ,Bo'stonliq"));
                    buttongaVatBnJonatish(user.getChatId(), 41.29962474317497, 69.24013777030345, "Toshkent ,Bo'stonliq", "Bo'stonliq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("yangiyol")) { // 41.106601409336605, 69.00099564918969
//                    sendMessage(user.getChatId().toString(), setApi(41.106601409336605, 69.00099564918969, "Toshkent ,Yangiyo'l"));
                    buttongaVatBnJonatish(user.getChatId(), 41.106601409336605, 69.00099564918969, "Toshkent ,Yangiyo'l", "Yangiyo'l");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("zangota")) { // 41.19119250475624, 69.14481581613583
//                    sendMessage(user.getChatId().toString(), setApi(41.19119250475624, 69.14481581613583, "Toshkent ,Zangiota"));
                    buttongaVatBnJonatish(user.getChatId(), 41.19119250475624, 69.14481581613583, "Toshkent ,Zangiota", "Zangiota");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("ohangaron")) { // 40.91797520041142, 69.63525266877876
//                    sendMessage(user.getChatId().toString(), setApi(40.91797520041142, 69.63525266877876, "Toshkent ,Ohangaron"));
                    buttongaVatBnJonatish(user.getChatId(), 40.91797520041142, 69.63525266877876, "Toshkent ,Ohangaron", "Ohangaron");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("oqqq")) { // 40.88044288436734, 69.04266437650311
//                    sendMessage(user.getChatId().toString(), setApi(40.88044288436734, 69.04266437650311, "Toshkent ,Oqqo'rg'on"));
                    buttongaVatBnJonatish(user.getChatId(), 40.88044288436734, 69.04266437650311, "Toshkent ,Oqqo'rg'on", "Oqqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("ppp")) { // 41.297497902393545, 69.68451728729993
//                    sendMessage(user.getChatId().toString(), setApi(41.297497902393545, 69.68451728729993, "Toshkent ,Parkent"));
                    buttongaVatBnJonatish(user.getChatId(), 41.297497902393545, 69.68451728729993, "Toshkent ,Parkent", "Parkent");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("ortachirchiq")) { // 41.20590993388904, 69.27922461238849
//                    sendMessage(user.getChatId().toString(), setApi(41.20590993388904, 69.27922461238849, "Toshkent ,O'rta Chirchiq"));
                    buttongaVatBnJonatish(user.getChatId(), 41.20590993388904, 69.27922461238849, "Toshkent ,O'rta Chirchiq", "O'rta Chirchiq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("yuqorichirchiq")) { // 41.20911424680261, 69.49407488275496
//                    sendMessage(user.getChatId().toString(), setApi(41.20911424680261, 69.49407488275496, "Toshkent ,Yuqori Chirchiq"));
                    buttongaVatBnJonatish(user.getChatId(), 41.20911424680261, 69.49407488275496, "Toshkent ,Yuqori Chirchiq", "Yuqori Chirchiq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("bekobod")) { // 40.454055968860445, 69.1819780480247
//                    sendMessage(user.getChatId().toString(), setApi(40.454055968860445, 69.1819780480247, "Toshkent ,Bekobod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.454055968860445, 69.1819780480247, "Toshkent ,Bekobod", "Bekobod");
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_fargona)) {
                if (data.equals("Farg???ona shahar")) { //40.37949214005235, 71.78153222147638
//                    sendMessage(user.getChatId().toString(), setApi(40.37949214005235, 71.78153222147638, "Farg'ona, Farg???ona shahar"));
                    buttongaVatBnJonatish(user.getChatId(), 40.37949214005235, 71.78153222147638, "Farg'ona, Farg???ona shahar", "Farg'ona");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Uchko???prik")) { //40.53267491097347, 71.02892566146784
//                    sendMessage(user.getChatId().toString(), setApi(40.53267491097347, 71.02892566146784, "Farg'ona, Uchko???prik"));
                    buttongaVatBnJonatish(user.getChatId(), 40.53267491097347, 71.02892566146784, "Farg'ona, Uchko???prik", "Uchko???prik");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("So???x")) { //39.968927659480165, 71.12908694248515
//                    sendMessage(user.getChatId().toString(), setApi(39.968927659480165, 71.12908694248515, "Farg'ona, So???x"));
                    buttongaVatBnJonatish(user.getChatId(), 39.968927659480165, 71.12908694248515, "Farg'ona, So???x", "So???x");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Yozyovon")) { //40.62131813678478, 71.6480687836224
//                    sendMessage(user.getChatId().toString(), setApi(40.62131813678478, 71.6480687836224, "Farg'ona, Yozyovon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.62131813678478, 71.6480687836224, "Farg'ona, Yozyovon", "Yozyovon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Quva")) { //40.524573367810504, 72.06257627873157
//                    sendMessage(user.getChatId().toString(), setApi(40.524573367810504, 72.06257627873157, "Farg'ona, Quva"));
                    buttongaVatBnJonatish(user.getChatId(), 40.524573367810504, 72.06257627873157, "Farg'ona, Quva", "Quva");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qo???qon")) { //40.524621966621304, 70.93356479162965
//                    sendMessage(user.getChatId().toString(), setApi(40.524621966621304, 70.93356479162965, "Farg'ona, Qo???qon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.524621966621304, 70.93356479162965, "Farg'ona, Qo???qon", "Qo???qon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Beshariq")) { //40.39690886265036, 70.57359975866977
//                    sendMessage(user.getChatId().toString(), setApi(40.39690886265036, 70.57359975866977, "Farg'ona, Beshariq"));
                    buttongaVatBnJonatish(user.getChatId(), 40.39690886265036, 70.57359975866977, "Farg'ona, Beshariq", "Beshariq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Quvasoy")) { //
//                    sendMessage(user.getChatId().toString(), setApi(40.30222, 71.97444, "Farg'ona, Quvasoy"));
                    buttongaVatBnJonatish(user.getChatId(), 40.30222, 71.97444, "Farg'ona, Quvasoy", "Quvasoy");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Farg???ona")) { //40.320714145540286, 71.70209565835899
//                    sendMessage(user.getChatId().toString(), setApi(40.320714145540286, 71.70209565835899, "Farg'ona, Farg???ona"));
                    buttongaVatBnJonatish(user.getChatId(), 40.320714145540286, 71.70209565835899, "Farg'ona, Farg???ona", "Farg???ona");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Toshloq")) { //40.56269866588281, 71.84044642257277
//                    sendMessage(user.getChatId().toString(), setApi(40.56269866588281, 71.84044642257277, "Farg'ona, Toshloq"));
                    buttongaVatBnJonatish(user.getChatId(), 40.56269866588281, 71.84044642257277, "Farg'ona, Toshloq", "Toshloq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("O???zbekiston")) { //40.330297796679396, 70.85868054864669
//                    sendMessage(user.getChatId().toString(), setApi(40.330297796679396, 70.85868054864669, "Farg'ona, O???zbekiston"));
                    buttongaVatBnJonatish(user.getChatId(), 40.330297796679396, 70.85868054864669, "Farg'ona, O???zbekiston", "O???zbekiston");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qo???shtepa")) { //40.53692345413236, 71.6415020647065
//                    sendMessage(user.getChatId().toString(), setApi(40.53692345413236, 71.6415020647065, "Farg'ona, Qo???shtepa"));
                    buttongaVatBnJonatish(user.getChatId(), 40.53692345413236, 71.6415020647065, "Farg'ona, Qo???shtepa", "Qo???shtepa");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Marg???ilon")) { //40.47635685369382, 71.71252435127899
//                    sendMessage(user.getChatId().toString(), setApi(40.47635685369382, 71.71252435127899, "Farg'ona, Marg???ilon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.47635685369382, 71.71252435127899, "Farg'ona, Marg???ilon", "Marg???ilon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Buvayda")) { //40.645693110093404, 71.10204505839337
//                    sendMessage(user.getChatId().toString(), setApi(40.645693110093404, 71.10204505839337, "Farg'ona, Buvayda"));
                    buttongaVatBnJonatish(user.getChatId(), 40.645693110093404, 71.10204505839337, "Farg'ona, Buvayda", "Buvayda");
                    userService.updateData(user, user.getId(), data);
                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Dang???ara")) { //40.58139710281787, 70.91477380795894
//                    sendMessage(user.getChatId().toString(), setApi(40.58139710281787, 70.91477380795894, "Farg'ona, Dang???ara"));
                    buttongaVatBnJonatish(user.getChatId(), 40.58139710281787, 70.91477380795894, "Farg'ona, Dang???ara", "Dang???ara");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Oltiariq")) { //40.39370084174377, 71.48670380912914
//                    sendMessage(user.getChatId().toString(), setApi(40.39370084174377, 71.48670380912914, "Farg'ona, Oltiariq"));
                    buttongaVatBnJonatish(user.getChatId(), 40.39370084174377, 71.48670380912914, "Farg'ona, Oltiariq", "Oltiariq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Rishton")) { //40.36410679785719, 71.27208639645364
//                    sendMessage(user.getChatId().toString(), setApi(40.36410679785719, 71.27208639645364, "Farg'ona, Rishton"));
                    buttongaVatBnJonatish(user.getChatId(), 40.36410679785719, 71.27208639645364, "Farg'ona, Rishton", "Rishton");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Furqat")) { //40.51673390388502, 70.71177286893743
//                    sendMessage(user.getChatId().toString(), setApi(40.51673390388502, 70.71177286893743, "Farg'ona, Furqat"));
                    buttongaVatBnJonatish(user.getChatId(), 40.51673390388502, 70.71177286893743, "Farg'ona, Furqat", "Furqat");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Bag???dod")) { //40.49387968871484, 71.20366862141532
//                    sendMessage(user.getChatId().toString(), setApi(40.49387968871484, 71.20366862141532, "Farg'ona, Bag???dod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.49387968871484, 71.20366862141532, "Farg'ona, Bag???dod", "Bag???dod");
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_andijon)) {
                if (data.equals("Andijon shahri")) {//40.7865585435806, 72.31208083702941
//                    sendMessage(user.getChatId().toString(), setApi(40.7865585435806, 72.31208083702941, "Andijon, Andijon shahar"));
                    buttongaVatBnJonatish(user.getChatId(), 40.7865585435806, 72.31208083702941, "Andijon, Andijon shahar", "Andijon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xonobod")) { //40.79989825348597, 72.98951560333182
//                    sendMessage(user.getChatId().toString(), setApi(40.454055968860445, 72.98951560333182, "Andijon, Xonobod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.454055968860445, 72.98951560333182, "Andijon, Xonobod", "Xonobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Asaka")) {// 40.6619355989725, 72.24428848141633
//                    sendMessage(user.getChatId().toString(), setApi(40.6619355989725, 72.24428848141633, "Andijon, Asaka"));
                    buttongaVatBnJonatish(user.getChatId(), 40.6619355989725, 72.24428848141633, "Andijon, Asaka", "Asaka");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Baliqchi")) {//40.8681067477643, 71.90901428107483
//                    sendMessage(user.getChatId().toString(), setApi(40.8681067477643, 71.90901428107483, "Andijon, Baliqchi"));
                    buttongaVatBnJonatish(user.getChatId(), 40.8681067477643, 71.90901428107483, "Andijon, Baliqchi", "Baliqchi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Bo'z")) { //40.67522368198782, 71.90202999274003
//                    sendMessage(user.getChatId().toString(), setApi(40.67522368198782, 71.90202999274003, "Andijon, Bo'z"));
                    buttongaVatBnJonatish(user.getChatId(), 40.67522368198782, 71.90202999274003, "Andijon, Bo'z", "Bo'z");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Buloqboshi")) { //40.61824097722169, 72.4589485266868
//                    sendMessage(user.getChatId().toString(), setApi(40.61824097722169, 72.4589485266868, "Andijon, Buloqboshi"));
                    buttongaVatBnJonatish(user.getChatId(), 40.61824097722169, 72.4589485266868, "Andijon, Buloqboshi", "Buloqboshi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Izboskan")) { //40.926914438296166, 72.23046114826215
//                    sendMessage(user.getChatId().toString(), setApi(40.926914438296166, 72.23046114826215, "Andijon, Izboskan"));
                    buttongaVatBnJonatish(user.getChatId(), 40.926914438296166, 72.23046114826215, "Andijon, Izboskan", "Izboskan");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Jalolquduq")) {//40.73260859991856, 72.61228519306135
//                    sendMessage(user.getChatId().toString(), setApi(40.73260859991856, 72.61228519306135, "Andijon, Jalolquduq"));
                    buttongaVatBnJonatish(user.getChatId(), 40.73260859991856, 72.61228519306135, "Andijon, Jalolquduq", "Jalolquduq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Marhamat")) { //40.51393917604782, 72.29981218880005
//                    sendMessage(user.getChatId().toString(), setApi(40.51393917604782, 72.29981218880005, "Andijon, Marhamat"));
                    buttongaVatBnJonatish(user.getChatId(), 40.51393917604782, 72.29981218880005, "Andijon, Marhamat", "Marhamat");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Oltinko'l")) {// 40.78257767662204, 72.15630346953014
//                    sendMessage(user.getChatId().toString(), setApi(40.78257767662204, 72.15630346953014, "Andijon, Oltinko'l"));
                    buttongaVatBnJonatish(user.getChatId(), 40.78257767662204, 72.15630346953014, "Andijon, Oltinko'l", "Oltinko'l");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Paxtaobod")) { //40.977855409832266, 72.42257164080198
//                    sendMessage(user.getChatId().toString(), setApi(40.977855409832266, 72.42257164080198, "Andijon, Paxtaobod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.977855409832266, 72.42257164080198, "Andijon, Paxtaobod", "Paxtaobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qo'rg'ontepa")) { // 40.737458816612616, 72.80489172467401
//                    sendMessage(user.getChatId().toString(), setApi(40.737458816612616, 72.80489172467401, "Andijon, Qo'rg'ontepa"));
                    buttongaVatBnJonatish(user.getChatId(), 40.737458816612616, 72.80489172467401, "Andijon, Qo'rg'ontepa", "Qo'rg'ontepa");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Ulug'nor")) { //40.7812938834895, 71.65829940832162
//                    sendMessage(user.getChatId().toString(), setApi(40.7812938834895, 71.65829940832162, "Andijon, Ulug'nor"));
                    buttongaVatBnJonatish(user.getChatId(), 40.7812938834895, 71.65829940832162, "Andijon, Ulug'nor", "Ulug'nor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xo'jaobod")) { // 40.644265790848365, 72.59695277036937
//                    sendMessage(user.getChatId().toString(), setApi(40.644265790848365, 72.59695277036937, "Andijon, Xo'jaobod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.644265790848365, 72.59695277036937, "Andijon, Xo'jaobod", "Bag???Xo'jaobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Shahrixon")) { //40.705658566969085, 72.04954603823948
//                    sendMessage(user.getChatId().toString(), setApi(40.705658566969085, 72.04954603823948, "Andijon, Shahrixon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.705658566969085, 72.04954603823948, "Andijon, Shahrixon", "Shahrixon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_samarqand)) {
                if (data.equals("Samarqand shahri")) {//39.65329327101519, 66.95826234725476
//                    sendMessage(user.getChatId().toString(), setApi(39.65329327101519, 66.95826234725476, "Samarqand, Samarqand shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 39.65329327101519, 66.95826234725476, "Samarqand, Samarqand shahri", "Samarqand");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Bulung'ur")) {//39.70791766177822, 67.28142857964608
//                    sendMessage(user.getChatId().toString(), setApi(39.70791766177822, 67.28142857964608, "Samarqand, Bulung'ur"));
                    buttongaVatBnJonatish(user.getChatId(), 39.70791766177822, 67.28142857964608, "Samarqand, Bulung'ur", "Bulung'ur");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Ishtixon")) {//39.98829349793204, 66.53144028860048
//                    sendMessage(user.getChatId().toString(), setApi(39.98829349793204, 66.53144028860048, "Samarqand, Ishtixon"));
                    buttongaVatBnJonatish(user.getChatId(), 39.98829349793204, 66.53144028860048, "Samarqand, Ishtixon", "Ishtixon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Jomboy")) {//39.76625238583371, 67.07779535428304
//                    sendMessage(user.getChatId().toString(), setApi(39.76625238583371, 67.07779535428304, "Samarqand, Jomboy"));
                    buttongaVatBnJonatish(user.getChatId(), 39.76625238583371, 67.07779535428304, "Samarqand, Jomboy", "Jomboy");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kattaqo'rg'on tumani")) { // 39.981981100516265, 66.2896029199572
//                    sendMessage(user.getChatId().toString(), setApi(39.981981100516265, 66.2896029199572, "Samarqand, Kattaqo'rg'on shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 39.981981100516265, 66.2896029199572, "Samarqand, Kattaqo'rg'on shahri", "Kattaqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kattaqo'rg'on shahri")) { // 39.8993149735457, 66.2633149577346
//                    sendMessage(user.getChatId().toString(), setApi(39.8993149735457, 66.2633149577346, "Samarqand, Kattaqo'rg'on tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 39.8993149735457, 66.2633149577346, "Samarqand, Kattaqo'rg'on tumani", "Kattaqo'rg'on");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Narpay")) {//39.92524679769102, 65.98627826494945
//                    sendMessage(user.getChatId().toString(), setApi(39.92524679769102, 65.98627826494945, "Samarqand, Narpay"));
                    buttongaVatBnJonatish(user.getChatId(), 39.92524679769102, 65.98627826494945, "Samarqand, Narpay", "Narpay");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Nurobod")) {//39.57127098105206, 66.02799000748902
//                    sendMessage(user.getChatId().toString(), setApi(39.57127098105206, 66.02799000748902, "Samarqand, Nurobod"));
                    buttongaVatBnJonatish(user.getChatId(), 39.57127098105206, 66.02799000748902, "Samarqand, Nurobod", "Nurobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Oqdaryo")) {//39.839873784601465, 66.73834694617673
//                    sendMessage(user.getChatId().toString(), setApi(39.839873784601465, 66.73834694617673, "Samarqand, Oqdaryo"));
                    buttongaVatBnJonatish(user.getChatId(), 39.839873784601465, 66.73834694617673, "Samarqand, Oqdaryo", "Oqdaryo");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Past darg'om")) { // 39.704352292734065, 66.62242053041332
//                    sendMessage(user.getChatId().toString(), setApi(39.704352292734065, 66.62242053041332, "Samarqand, Past darg'om"));
                    buttongaVatBnJonatish(user.getChatId(), 39.704352292734065, 66.62242053041332, "Samarqand, Past darg'om", "Past darg'om");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Urgut")) {//39.447002526177904, 67.17173665735707
//                    sendMessage(user.getChatId().toString(), setApi(39.447002526177904, 67.17173665735707, "Samarqand, Urgut"));
                    buttongaVatBnJonatish(user.getChatId(), 39.447002526177904, 67.17173665735707, "Samarqand, Urgut", "Urgut");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Paxtachi")) {//39.888515759299004, 65.6089318735053
//                    sendMessage(user.getChatId().toString(), setApi(39.888515759299004, 65.6089318735053, "Samarqand, Paxtachi"));
                    buttongaVatBnJonatish(user.getChatId(), 39.888515759299004, 65.6089318735053, "Samarqand, Paxtachi", "Paxtachi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Poyariq")) {//40.05104556455984, 66.85419779064077
//                    sendMessage(user.getChatId().toString(), setApi(40.05104556455984, 66.85419779064077, "Samarqand, Poyariq"));
                    buttongaVatBnJonatish(user.getChatId(), 40.05104556455984, 66.85419779064077, "Samarqand, Poyariq", "Poyariq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Samarqand tumani")) { //39.59818617036431, 66.90471762188967
//                    sendMessage(user.getChatId().toString(), setApi(39.59818617036431, 66.90471762188967, "Samarqand, Samarqand tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 39.59818617036431, 66.90471762188967, "Samarqand, Samarqand tumani", "Samarqand");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Toyloq")) { //39.56442359774873, 67.14018598905683
//                    sendMessage(user.getChatId().toString(), setApi(39.56442359774873, 67.14018598905683, "Samarqand, Toyloq"));
                    buttongaVatBnJonatish(user.getChatId(), 39.56442359774873, 67.14018598905683, "Samarqand, Toyloq", "Toyloq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qo'shrabot")) { //40.38655726843597, 66.5100175021684
//                    sendMessage(user.getChatId().toString(), setApi(40.38655726843597, 66.5100175021684, "Samarqand, Qo'shrabot"));
                    buttongaVatBnJonatish(user.getChatId(), 40.38655726843597, 66.5100175021684, "Samarqand, Qo'shrabot", "Qo'shrabot");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_jizzah)) {
                if (data.equals("Jizzax shahri")) {//40.11977007458388, 67.85232343283624
//                    sendMessage(user.getChatId().toString(), setApi(40.11977007458388, 67.85232343283624, "Jizzah, Jizzax shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 40.11977007458388, 67.85232343283624, "Jizzah, Jizzax shahri", "Jizzax shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Arnasoy")) { //40.59901232957254, 67.79358090175431
//                    sendMessage(user.getChatId().toString(), setApi(40.59901232957254, 67.79358090175431, "Jizzah, Arnasoy"));
                    buttongaVatBnJonatish(user.getChatId(), 40.59901232957254, 67.79358090175431, "Jizzah, Arnasoy", "Arnasoy");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Baxmal")) {//39.78225382213002, 67.7250016175458
//                    sendMessage(user.getChatId().toString(), setApi(39.78225382213002, 67.7250016175458, "Jizzah, Baxmal"));
                    buttongaVatBnJonatish(user.getChatId(), 39.78225382213002, 67.7250016175458, "Jizzah, Baxmal", "Baxmal");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Forish")) {//40.70901562393393, 67.18181520202268
//                    sendMessage(user.getChatId().toString(), setApi(40.70901562393393, 67.18181520202268, "Jizzah, Forish"));
                    buttongaVatBnJonatish(user.getChatId(), 40.70901562393393, 67.18181520202268, "Jizzah, Forish", "Forish");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("G'allaorol")) { //40.10205495202923, 67.32846413281408
//                    sendMessage(user.getChatId().toString(), setApi(40.10205495202923, 67.32846413281408, "Jizzah, G'allaorol"));
                    buttongaVatBnJonatish(user.getChatId(), 40.10205495202923, 67.32846413281408, "Jizzah, G'allaorol", "G'allaorol");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Do'stlik")) { //40.58181976770775, 68.04548305263552
//                    sendMessage(user.getChatId().toString(), setApi(40.58181976770775, 68.04548305263552, "Jizzah, Do'stlik"));
                    buttongaVatBnJonatish(user.getChatId(), 40.58181976770775, 68.04548305263552, "Jizzah, Do'stlik", "Do'stlik");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Jizzax tumani")) { //40.11455358141378, 67.82473708142044
//                    sendMessage(user.getChatId().toString(), setApi(40.11455358141378, 67.82473708142044, "Jizzah, Jizzax tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 40.11455358141378, 67.82473708142044, "Jizzah, Jizzax tumani", "Jizzax tumani");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Mirzacho'l")) {//40.724412215707, 68.06118184566003
//                    sendMessage(user.getChatId().toString(), setApi(40.724412215707, 68.06118184566003, "Jizzah, Mirzacho'l"));
                    buttongaVatBnJonatish(user.getChatId(), 40.724412215707, 68.06118184566003, "Jizzah, Mirzacho'l", "Mirzacho'l");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Paxtakor")) { //40.35950221136383, 68.00475878330718
//                    sendMessage(user.getChatId().toString(), setApi(40.35950221136383, 68.00475878330718, "Jizzah, Paxtakor"));
                    buttongaVatBnJonatish(user.getChatId(), 40.35950221136383, 68.00475878330718, "Jizzah, Paxtakor", "Paxtakor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Yangiobod")) {//39.99243484998208, 68.74527110184545
//                    sendMessage(user.getChatId().toString(), setApi(39.99243484998208, 68.74527110184545, "Jizzah, Yangiobod"));
                    buttongaVatBnJonatish(user.getChatId(), 39.99243484998208, 68.74527110184545, "Jizzah, Yangiobod", "Yangiobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Zafarobod")) { //40.41330581212753, 67.76451866402623
//                    sendMessage(user.getChatId().toString(), setApi(40.41330581212753, 67.76451866402623, "Jizzah, Zafarobod"));
                    buttongaVatBnJonatish(user.getChatId(), 40.41330581212753, 67.76451866402623, "Jizzah, Zafarobod", "Zafarobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Zarband")) { //40.189983986463055, 68.11455808514548
//                    sendMessage(user.getChatId().toString(), setApi(40.189983986463055, 68.11455808514548, "Jizzah, Zarband"));
                    buttongaVatBnJonatish(user.getChatId(), 40.189983986463055, 68.11455808514548, "Jizzah, Zarband", "Zarband");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Zomin")) { //39.88553183671697, 68.3643022291486
//                    sendMessage(user.getChatId().toString(), setApi(39.88553183671697, 68.3643022291486, "Jizzah, Zomin"));
                    buttongaVatBnJonatish(user.getChatId(), 39.88553183671697, 68.3643022291486, "Jizzah, Zomin", "Zomin");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
//                sendKunOy(user.getChatId(), data);
            } else if (user.getStep().equals(UserStep.select_lang_lt_buxoro)) {
                if (data.equals("Buxoro shahri")) {//39.77672367865153, 64.4210785732946
//                    sendMessage(user.getChatId().toString(), setApi(39.77672367865153, 64.4210785732946, "Buxoro, Buxoro shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 39.77672367865153, 64.4210785732946, "Buxoro, Buxoro shahri", "Buxoro shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Buxoro tumani")) { //39.63566090876281, 64.30230294221505
//                    sendMessage(user.getChatId().toString(), setApi(39.63566090876281, 64.30230294221505, "Buxoro, Buxoro tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 39.63566090876281, 64.30230294221505, "Buxoro, Buxoro tumani", "Buxoro tumani");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("G'ijduvon")) {//40.601906240839995, 64.71053498588797
//                    sendKunOy(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApi(40.601906240839995, 64.71053498588797, "Buxoro, G'ijduvon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.601906240839995, 64.71053498588797, "Buxoro, G'ijduvon", "G'ijduvon");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Jondor")) {//39.971219891973426, 63.5714954269693
//                    sendMessage(user.getChatId().toString(), setApi(39.971219891973426, 63.5714954269693, "Buxoro, Jondor"));
                    buttongaVatBnJonatish(user.getChatId(), 39.971219891973426, 63.5714954269693, "Buxoro, Jondor", "Jondor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kogon shahri")) { //39.72355224686954, 64.55132893267286
//                    sendMessage(user.getChatId().toString(), setApi(39.72355224686954, 64.55132893267286, "Buxoro, Kogon shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 39.72355224686954, 64.55132893267286, "Buxoro, Kogon shahri", "Kogon shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kogon tumani")) { //39.69809076622412, 64.53702710674044
//                    sendMessage(user.getChatId().toString(), setApi(39.69809076622412, 64.53702710674044, "Buxoro, Kogon tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 39.69809076622412, 64.53702710674044, "Buxoro, Kogon tumani", "Kogon tumani");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Olot")) { //39.281691000170674, 64.03768298578096
//                    sendMessage(user.getChatId().toString(), setApi(39.281691000170674, 64.03768298578096, "Buxoro, Olot"));
                    buttongaVatBnJonatish(user.getChatId(), 39.281691000170674, 64.03768298578096, "Buxoro, Olot", "Olot");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Peshku")) {//40.73303621917936, 63.21959281775061
//                    sendMessage(user.getChatId().toString(), setApi(40.73303621917936, 63.21959281775061, "Buxoro, Peshku"));
                    buttongaVatBnJonatish(user.getChatId(), 40.73303621917936, 63.21959281775061, "Buxoro, Peshku", "Peshku");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qorako'l")) { //39.936970007976, 62.941145138407805
//                    sendMessage(user.getChatId().toString(), setApi(39.936970007976, 62.941145138407805, "Buxoro, Qorako'l"));
                    buttongaVatBnJonatish(user.getChatId(), 39.936970007976, 62.941145138407805, "Buxoro, Qorako'l", "Qorako'l");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qorovulbozor")) {//39.46527348718642, 64.67070586334809
//                    sendMessage(user.getChatId().toString(), setApi(39.46527348718642, 64.67070586334809, "Buxoro, Qorovulbozor"));
                    buttongaVatBnJonatish(user.getChatId(), 39.46527348718642, 64.67070586334809, "Buxoro, Qorovulbozor", "Qorovulbozor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Romitan")) { //40.731071302974954, 62.530718928951856
//                    sendMessage(user.getChatId().toString(), setApi(40.731071302974954, 62.530718928951856, "Buxoro, Romitan"));
                    buttongaVatBnJonatish(user.getChatId(), 40.731071302974954, 62.530718928951856, "Buxoro, Romitan", "Romitan");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Shofirkon")) { // 40.12540773474785, 64.48270729594604
//                    sendMessage(user.getChatId().toString(), setApi(40.12540773474785, 64.48270729594604, "Buxoro, Shofirkon"));
                    buttongaVatBnJonatish(user.getChatId(), 40.12540773474785, 64.48270729594604, "Buxoro, Shofirkon", "Shofirkon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Vobkent")) { //40.15547517789773, 64.53244365200904
//                    sendMessage(user.getChatId().toString(), setApi(40.15547517789773, 64.53244365200904, "Buxoro, Vobkent"));
                    buttongaVatBnJonatish(user.getChatId(), 40.15547517789773, 64.53244365200904, "Buxoro, Vobkent", "Buxoro");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_xorazm)) {
                if (data.equals("Bog'ot")) {//41.33744153309746, 60.87694940015311
//                    sendMessage(user.getChatId().toString(), setApi(41.33744153309746, 60.87694940015311, "Xorazm, Bog'ot"));
                    buttongaVatBnJonatish(user.getChatId(), 41.33744153309746, 60.87694940015311, "Xorazm, Bog'ot", "Bog'ot");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
                if (data.equals("Gurlan")) {//41.90536583933732, 60.37982747138814
//                    sendMessage(user.getChatId().toString(), setApi(41.90536583933732, 60.37982747138814, "Xorazm, Gurlan"));
                    buttongaVatBnJonatish(user.getChatId(), 41.90536583933732, 60.37982747138814, "Xorazm, Gurlan", "Gurlan");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Shovot")) {//41.710201238586826, 60.27012648475865
//                    sendMessage(user.getChatId().toString(), setApi(41.710201238586826, 60.27012648475865, "Xorazm, Shovot"));
                    buttongaVatBnJonatish(user.getChatId(), 41.710201238586826, 60.27012648475865, "Xorazm, Shovot", "Shovot");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qo'shko'pir")) {//41.526982196908484, 60.269577660413084
//                    sendMessage(user.getChatId().toString(), setApi(41.526982196908484, 60.269577660413084, "Xorazm, Qo'shko'pir"));
                    buttongaVatBnJonatish(user.getChatId(), 41.526982196908484, 60.269577660413084, "Xorazm, Qo'shko'pir", "Qo'shko'pir");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Yangibozor")) {// 41.75025881608148, 60.54049313655263
//                    sendMessage(user.getChatId().toString(), setApi(41.75025881608148, 60.54049313655263, "Xorazm, Yangibozor"));
                    buttongaVatBnJonatish(user.getChatId(), 41.75025881608148, 60.54049313655263, "Xorazm, Yangibozor", "Yangibozor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Urganch shahri")) {//41.547992018041974, 60.618264246685676
//                    sendMessage(user.getChatId().toString(), setApi(41.547992018041974, 60.618264246685676, "Xorazm, Urganch shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 41.547992018041974, 60.618264246685676, "Xorazm, Urganch shahri", "Urganch shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Urganch tumani")) {//41.61625437893366, 60.533593007029786
//                    sendMessage(user.getChatId().toString(), setApi(41.61625437893366, 60.533593007029786, "Xorazm, Urganch tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 41.61625437893366, 60.533593007029786, "Xorazm, Urganch tumani", "Urganch tumani");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xazorasp")) {//41.30448683950074, 61.09745692690238
//                    sendMessage(user.getChatId().toString(), setApi(41.30448683950074, 61.09745692690238, "Xorazm, Xazorasp"));
                    buttongaVatBnJonatish(user.getChatId(), 41.30448683950074, 61.09745692690238, "Xorazm, Xazorasp", "Xazorasp");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xiva")) {//41.39184570391674, 60.34725590732044
//                    sendMessage(user.getChatId().toString(), setApi(41.39184570391674, 60.34725590732044, "Xorazm, Xiva"));
                    buttongaVatBnJonatish(user.getChatId(), 41.39184570391674, 60.34725590732044, "Xorazm, Xiva", "Xiva");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xonqa")) {//41.481393767464844, 60.829570881655584
//                    sendMessage(user.getChatId().toString(), setApi(41.481393767464844, 60.829570881655584, "Xorazm, Xonqa"));
                    buttongaVatBnJonatish(user.getChatId(), 41.481393767464844, 60.829570881655584, "Xorazm, Xonqa", "Xonqa");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Yangiariq")) {// 41.346206916399666, 60.54804614851794
//                    sendMessage(user.getChatId().toString(), setApi(41.346206916399666, 60.54804614851794, "Xorazm, Yangiariq"));
                    buttongaVatBnJonatish(user.getChatId(), 41.346206916399666, 60.54804614851794, "Xorazm, Yangiariq", "Yangiariq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_navoiy)) {
                if (data.equals("Navoiy shahri")) {//40.10166507561083, 65.3575344176788
//                    sendMessage(user.getChatId().toString(), setApi(40.10166507561083, 65.3575344176788, "Navoiy, Navoiy shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 40.10166507561083, 65.3575344176788, "Navoiy, Navoiy shahri", "Navoiy shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Karmana")) {//40.14372322026956, 65.35467198347476
//                    sendMessage(user.getChatId().toString(), setApi(40.14372322026956, 65.35467198347476, "Navoiy, Karmana"));
                    buttongaVatBnJonatish(user.getChatId(), 40.14372322026956, 65.35467198347476, "Navoiy, Karmana", "Karmana");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Konimex")) {// 40.277684998973186, 65.14210146382663
//                    sendMessage(user.getChatId().toString(), setApi(40.27768499897318, 65.1421014638266, "Navoiy, Konimex"));
                    buttongaVatBnJonatish(user.getChatId(), 40.27768499897318, 65.1421014638266, "Navoiy, Konimex", "Konimex");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Navbahor")) {//40.232621293600054, 65.23269831157204
//                    sendMessage(user.getChatId().toString(), setApi(40.232621293600054, 65.23269831157204, " Navoiy, Navbahor"));
                    buttongaVatBnJonatish(user.getChatId(), 40.232621293600054, 65.23269831157204, " Navoiy, Navbahor", "Navbahor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Nurota")) {//40/.61096195334514, 65.93817362794783
//                    sendMessage(user.getChatId().toString(), setApi(40.61096195334514, 65.93817362794783, "Navoiy, Nurota"));
                    buttongaVatBnJonatish(user.getChatId(), 40.61096195334514, 65.93817362794783, "Navoiy, Nurota", "Nurota");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qiziltepa")) {//39.8966432527515, 64.76528114807608
//                    sendMessage(user.getChatId().toString(), setApi(39.8966432527515, 64.76528114807608, "Navoiy, Qiziltepa"));
                    buttongaVatBnJonatish(user.getChatId(), 39.8966432527515, 64.76528114807608, "Navoiy, Qiziltepa", "Qiziltepa");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Tomdi")) {//42.29906147558487, 64.90150727134733
//                    sendMessage(user.getChatId().toString(), setApi(42.29906147558487, 64.90150727134733, "Navoiy, Tomdi"));
                    buttongaVatBnJonatish(user.getChatId(), 42.29906147558487, 64.90150727134733, "Navoiy, Tomdi", "Tomdi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Uchquduq")) {//42.44642264030942, 62.6945789709221
//                    sendMessage(user.getChatId().toString(), setApi(42.44642264030942, 62.6945789709221, "Navoiy, Uchquduq"));
                    buttongaVatBnJonatish(user.getChatId(), 42.44642264030942, 62.6945789709221, "Navoiy, Uchquduq", "Uchquduq");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Xatirchi")) {//40.230069075732, 65.9409253080852
//                    sendMessage(user.getChatId().toString(), setApi(40.230069075732, 65.9409253080852, "Navoiy, Xatirchi"));
                    buttongaVatBnJonatish(user.getChatId(), 40.230069075732, 65.9409253080852, "Navoiy, Xatirchi", "Xatirchi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Zarafshon")) {//41.574460147847816, 64.18331570807477
//                    sendMessage(user.getChatId().toString(), setApi(41.574460147847816, 64.18331570807477, "Navoiy, Zarafshon"));
                    buttongaVatBnJonatish(user.getChatId(), 41.574460147847816, 64.18331570807477, "Navoiy, Zarafshon", "Zarafshon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_lt_qashqadaryo)) {
                if (data.equals("Qarshi shahri")) {//38.86191354410989, 65.78491933285783
//                    sendMessage(user.getChatId().toString(), setApi(38.86191354410989, 65.78491933285783, "Qashqadaryo, Qarshi shahri"));
                    buttongaVatBnJonatish(user.getChatId(), 38.86191354410989, 65.78491933285783, "Qashqadaryo, Qarshi shahri", "Qarshi shahri");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qarshi tumani")) {//38.779267614177556, 65.75278333536036
//                    sendMessage(user.getChatId().toString(), setApi(38.779267614177556, 65.75278333536036, "Qashqadaryo, Qarshi tumani"));
                    buttongaVatBnJonatish(user.getChatId(), 38.779267614177556, 65.75278333536036, "Qashqadaryo, Qarshi tumani", "Qarshi tumani");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Chiroqchi")) {//39.18112111196543, 66.25891591391694
//                    sendMessage(user.getChatId().toString(), setApi(39.18112111196543, 66.25891591391694, "Qashqadaryo, Chiroqchi"));
                    buttongaVatBnJonatish(user.getChatId(), 39.18112111196543, 66.25891591391694, "Qashqadaryo, Chiroqchi", "Chiroqchi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Dehqonobod")) {//38.35730703486065, 66.47533103897435
//                    sendMessage(user.getChatId().toString(), setApi(38.35730703486065, 66.47533103897435, "Qashqadaryo, Dehqonobod"));
                    buttongaVatBnJonatish(user.getChatId(), 38.35730703486065, 66.47533103897435, "Qashqadaryo, Dehqonobod", "Dehqonobod");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("G'uzor")) {//38.587010422831746, 66.04489530163235
                    buttongaVatBnJonatish(user.getChatId(), 38.587010422831746, 66.04489530163235, "Qashqadaryo, G'uzor", "G'uzor");
//                    sendMessage(user.getChatId().toString(), setApi(38.587010422831746, 66.04489530163235, "Qashqadaryo, G'uzor"));
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kasbi")) {//38.93540618287719, 65.43383657129233
//                    sendMessage(user.getChatId().toString(), setApi(38.93540618287719, 65.43383657129233, "Qashqadaryo, KasbiKasbi"));
                    buttongaVatBnJonatish(user.getChatId(), 38.93540618287719, 65.43383657129233, "Qashqadaryo, KasbiKasbi", "KasbiKasbi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Kitob")) {// 39.2152414606226, 67.03598426370185
//                    sendMessage(user.getChatId().toString(), setApi(39.2152414606226, 67.03598426370185, "Qashqadaryo, Kitob"));
                    buttongaVatBnJonatish(user.getChatId(), 39.2152414606226, 67.03598426370185, "Qashqadaryo, Kitob", "Kitob");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Koson")) {// 39.16770611275984, 65.77183802497865
//                    sendMessage(user.getChatId().toString(), setApi(39.16770611275984, 65.77183802497865, "Qashqadaryo, Koson"));
                    buttongaVatBnJonatish(user.getChatId(), 39.16770611275984, 65.77183802497865, "Qashqadaryo, Koson", "Koson");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Mirishkor")) {// 38.874194821533756, 64.92111490671182
//                    sendMessage(user.getChatId().toString(), setApi(38.874194821533756, 64.92111490671182, "Qashqadaryo, Mirishkor"));
                    buttongaVatBnJonatish(user.getChatId(), 38.874194821533756, 64.92111490671182, "Qashqadaryo, Mirishkor", "Mirishkor");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Muborak")) {//39.30420398425692, 65.25997593377225
//                    sendMessage(user.getChatId().toString(), setApi(39.30420398425692, 65.25997593377225, "Qashqadaryo, Muborak"));
                    buttongaVatBnJonatish(user.getChatId(), 39.30420398425692, 65.25997593377225, "Qashqadaryo, Muborak", "Muborak");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Nishon")) {//38.53608390327857, 65.48400423357076
//                    sendMessage(user.getChatId().toString(), setApi(38.53608390327857, 65.48400423357076, "Qashqadaryo, Nishon"));
                    buttongaVatBnJonatish(user.getChatId(), 38.53608390327857, 65.48400423357076, "Qashqadaryo, Nishon", "Nishon");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Qamashi")) {// 38.75660026948466, 66.60185133772104
//                    sendMessage(user.getChatId().toString(), setApi(38.75660026948466, 66.60185133772104, "Qashqadaryo, Qamashi"));
                    buttongaVatBnJonatish(user.getChatId(), 38.75660026948466, 66.60185133772104, "Qashqadaryo, Qamashi", "Qamashi");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Shahrisabz")) {//38.99171024720661, 67.2009187750465
//                    sendMessage(user.getChatId().toString(), setApi(38.99171024720661, 67.2009187750465, "Qashqadaryo, Shahrisabz"));
                    buttongaVatBnJonatish(user.getChatId(), 38.99171024720661, 67.2009187750465, "Qashqadaryo, Shahrisabz", "Shahrisabz");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                } else if (data.equals("Yakkabog'")) {//38.90742740574835, 66.75665915180723
//                    sendMessage(user.getChatId().toString(), setApi(40574835, 66.38.90742775665915180723, "Qashqadaryo, Yakkabog'"));
                    buttongaVatBnJonatish(user.getChatId(), 38.90742740574835, 66.75665915180723, "Qashqadaryo, Yakkabog'", "Yakkabog'");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOy(user.getChatId(), data);
                }
            }
//            else if (user.getStep().equals(UserStep.select_lang_lt_sirdaryo)) {
//                if (data.equals("Boyovut")){ //
//                    buttongaVatBnJonatish(user.getChatId(),  , , "Sirdaryo, Boyovut", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Guliston shahri")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Guliston shahri", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Guliston tumani")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Guliston tumani", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Oqoltin")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Oqoltin", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Sardoba")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Sardoba", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Sayxunobod")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Sayxunobod", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Xovos")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Xovos", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Sirdaryo tumani")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Sirdaryo tumani", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Shirin")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Shirin", " ");
//                    userService.updateData(user, user.getId(), data);
//                }else if (data.equals("Yangiyer shahri")){ //
//                    buttongaVatBnJonatish(user.getChatId(), , , "Sirdaryo, Yangiyer shahri", " ");
//                    userService.updateData(user, user.getId(), data);
//                }
//            }
            else if (user.getStep().equals(UserStep.select_lang_krl)) {
                if (data.equals("location")) {
                    sendMessage(user.getChatId().toString(), "(KRL)Iltimos o'zingiz joylashgan joy lakatsiyasini botga tashlang...");
                    userService.update(user, user.getId(), UserStep.select_lang_krl_location);
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("andijon")) {
                    andijonTumansK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_andijon);
                } else if (data.equals("toshkent")) {
                    getToshkentKrl(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_toshkent);
                } else if (data.equals("samarqand")) {
                    getsamarqandKrl(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_samarqand);
                } else if (data.equals("buxoro")) {
                    getBuxoroK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_buxoro);
                } else if (data.equals("navoiy")) {
                    getNamvoiyK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_navoiy);
                } else if (data.equals("namangan")) {
                    namanganTumansK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_namangan);
                } else if (data.equals("fargona")) {
                    fagonaTumans(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_fargona);
                } else if (data.equals("jizzah")) {
                    setJizzahK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_jizzah);
                } else if (data.equals("xorazm")) {
                    setXorazmK(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_xorazm);
                } else if (data.equals("qashqadaryo")) {
                    getQashqa(user.getChatId());
                    userService.update(user, user.getId(), UserStep.select_lang_krl_qashqadaryo);
                }
            }
            //
            else if (user.getStep().equals(UserStep.select_lang_krl_andijon)) {
                if (data.equals("Andijon shahri")) {//40.7865585435806, 72.31208083702941
//                    sendMessage(user.getChatId().toString(), setApiK(40.7865585435806, 72.31208083702941, "A????????????, A???????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.7865585435806, 72.31208083702941, "A????????????, A???????????? ??????????", "A???????????? ??????????");
//                    sendKunOyKrla(user.getChatId(), data);
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Xonobod")) { //40.79989825348597, 72.98951560333182
//                    sendMessage(user.getChatId().toString(), setApiK(40.454055968860445, 72.98951560333182, "A????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.454055968860445, 72.98951560333182, "A????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Asaka")) {// 40.6619355989725, 72.24428848141633
//                    sendMessage(user.getChatId().toString(), setApiK(40.6619355989725, 72.24428848141633, "A????????????, A????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.6619355989725, 72.24428848141633, "A????????????, A????????", "A????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Baliqchi")) {//40.8681067477643, 71.90901428107483
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.8681067477643, 71.90901428107483, "A???????????? ,??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Bo'z")) { //40.67522368198782, 71.90202999274003
//                    sendMessage(user.getChatId().toString(), setApiK(40.67522368198782, 71.90202999274003, "A????????????, ??????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.67522368198782, 71.90202999274003, "A????????????, ??????", "??????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Buloqboshi")) { //40.61824097722169, 72.4589485266868
//                    sendMessage(user.getChatId().toString(), setApiK(40.61824097722169, 72.4589485266868, "A????????????, ??????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.61824097722169, 72.4589485266868, "A????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Izboskan")) { //40.926914438296166, 72.23046114826215
//                    sendMessage(user.getChatId().toString(), setApiK(40.926914438296166, 72.23046114826215, "A????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.926914438296166, 72.23046114826215, "A????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Jalolquduq")) {//40.73260859991856, 72.61228519306135
//                    sendMessage(user.getChatId().toString(), setApiK(40.73260859991856, 72.61228519306135, "A????????????, ????????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.73260859991856, 72.61228519306135, "A????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Marhamat")) { //40.51393917604782, 72.29981218880005
//                    sendMessage(user.getChatId().toString(), setApiK(40.51393917604782, 72.29981218880005, "A????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.51393917604782, 72.29981218880005, "A????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Oltinko'l")) {// 40.78257767662204, 72.15630346953014
//                    sendMessage(user.getChatId().toString(), setApiK(40.78257767662204, 72.15630346953014, "A????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.78257767662204, 72.15630346953014, "A????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Paxtaobod")) { //40.977855409832266, 72.42257164080198
//                    sendMessage(user.getChatId().toString(), setApiK(40.977855409832266, 72.42257164080198, "A????????????, ??????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.977855409832266, 72.42257164080198, "A????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qo'rg'ontepa")) { // 40.737458816612616, 72.80489172467401
//                    sendMessage(user.getChatId().toString(), setApiK(40.737458816612616, 72.80489172467401, "A????????????, ????????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.737458816612616, 72.80489172467401, "A????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Ulug'nor")) { //40.7812938834895, 71.65829940832162
//                    sendMessage(user.getChatId().toString(), setApiK(40.7812938834895, 71.65829940832162, "A????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.7812938834895, 71.65829940832162, "A????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Xo'jaobod")) { // 40.644265790848365, 72.59695277036937
//                    sendMessage(user.getChatId().toString(), setApiK(40.644265790848365, 72.59695277036937, "A????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.644265790848365, 72.59695277036937, "A????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Shahrixon")) { //40.705658566969085, 72.04954603823948
//                    sendMessage(user.getChatId().toString(), setApiK(40.705658566969085, 72.04954603823948, "A????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.705658566969085, 72.04954603823948, "A????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_toshkent)) {

                if (data.equals("ToshknetSH")) { // 41.31862648082324, 69.26185247247427
//                    sendMessage(user.getChatId().toString(), setApiK(41.31862648082324, 69.26185247247427, "?????????????? ,?????????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.31862648082324, 69.26185247247427, "?????????????? ,?????????????? ??????????", "?????????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("chilonzor")) { // 41.25966068034852, 69.17981683014095
//                    sendMessage(user.getChatId().toString(), setApiK(41.25966068034852, 69.17981683014095, "?????????????? ,????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.25966068034852, 69.17981683014095, "?????????????? ,????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("quyiC")) { // 41.324249737732416, 69.27336159543128
//                    sendMessage(user.getChatId().toString(), setApiK(41.324249737732416, 69.27336159543128, "?????????????? ,???????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.324249737732416, 69.27336159543128, "?????????????? ,???????? ????????????", "???????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("bostonliq")) { // 41.29962474317497, 69.24013777030345
//                    sendMessage(user.getChatId().toString(), setApiK(41.29962474317497, 69.24013777030345, "?????????????? ,??????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.29962474317497, 69.24013777030345, "?????????????? ,??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("yangiyol")) { // 41.106601409336605, 69.00099564918969
//                    sendMessage(user.getChatId().toString(), setApiK(41.106601409336605, 69.00099564918969, "?????????????? ,??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.106601409336605, 69.00099564918969, "?????????????? ,??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("zangota")) { // 41.19119250475624, 69.14481581613583
//                    sendMessage(user.getChatId().toString(), setApiK(41.19119250475624, 69.14481581613583, "?????????????? ,????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.19119250475624, 69.14481581613583, "?????????????? ,????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("ohangaron")) { // 40.91797520041142, 69.63525266877876
//                    sendMessage(user.getChatId().toString(), setApiK(40.91797520041142, 69.63525266877876, "?????????????? ,??????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.91797520041142, 69.63525266877876, "?????????????? ,??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("oqqq")) { // 40.88044288436734, 69.04266437650311
//                    sendMessage(user.getChatId().toString(), setApiK(40.88044288436734, 69.04266437650311, "?????????????? ,????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.88044288436734, 69.04266437650311, "?????????????? ,????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("ppp")) { // 41.297497902393545, 69.68451728729993
//                    sendMessage(user.getChatId().toString(), setApiK(41.297497902393545, 69.68451728729993, "?????????????? ,??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.297497902393545, 69.68451728729993, "?????????????? ,??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("ortachirchiq")) { // 41.20590993388904, 69.27922461238849
//                    sendMessage(user.getChatId().toString(), setApiK(41.20590993388904, 69.27922461238849, "?????????????? ,???????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.20590993388904, 69.27922461238849, "?????????????? ,???????? ????????????", "???????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("yuqorichirchiq")) { // 41.20911424680261, 69.49407488275496
//                    sendMessage(user.getChatId().toString(), setApiK(41.20911424680261, 69.49407488275496, "?????????????? ,?????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.20911424680261, 69.49407488275496, "?????????????? ,?????????? ????????????", "?????????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("bekobod")) { // 40.454055968860445, 69.1819780480247
//                    sendMessage(user.getChatId().toString(), setApiK(40.454055968860445, 69.1819780480247, "?????????????? ,??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.454055968860445, 69.1819780480247, "?????????????? ,??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_samarqand)) {
                if (data.equals("Samarqand shahri")) {//39.65329327101519, 66.95826234725476
//                    sendMessage(user.getChatId().toString(), setApiK(39.65329327101519, 66.95826234725476, "??????????????????, ?????????????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.65329327101519, 66.95826234725476, "??????????????????, ?????????????????? ??????????", "?????????????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Bulung'ur")) {//39.70791766177822, 67.28142857964608
//                    sendMessage(user.getChatId().toString(), setApiK(39.70791766177822, 67.28142857964608, "??????????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.70791766177822, 67.28142857964608, "??????????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Ishtixon")) {//39.98829349793204, 66.53144028860048
//                    sendMessage(user.getChatId().toString(), setApiK(39.98829349793204, 66.53144028860048, "??????????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.98829349793204, 66.53144028860048, "??????????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Jomboy")) {//39.76625238583371, 67.07779535428304
//                    sendMessage(user.getChatId().toString(), setApiK(39.76625238583371, 67.07779535428304, "??????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.76625238583371, 67.07779535428304, "??????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Kattaqo'rg'on tumani")) { // 39.981981100516265, 66.2896029199572
//                    sendMessage(user.getChatId().toString(), setApiK(39.981981100516265, 66.2896029199572, "??????????????????, ?????????????????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.981981100516265, 66.2896029199572, "??????????????????, ?????????????????????? ??????????", "?????????????????????? ??????????");
//                    sendKunOyKrla(user.getChatId(), data);
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Kattaqo'rg'on shahri")) { // 39.8993149735457, 66.2633149577346
//                    sendMessage(user.getChatId().toString(), setApiK(39.8993149735457, 66.2633149577346, "??????????????????, ?????????????????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.8993149735457, 66.2633149577346, "??????????????????, ?????????????????????? ????????????", "?????????????????????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Narpay")) {//39.92524679769102, 65.98627826494945
//                    sendMessage(user.getChatId().toString(), setApiK(39.92524679769102, 65.98627826494945, "??????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.92524679769102, 65.98627826494945, "??????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Nurobod")) {//39.57127098105206, 66.02799000748902
//                    sendMessage(user.getChatId().toString(), setApiK(39.57127098105206, 66.02799000748902, "??????????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.57127098105206, 66.02799000748902, "??????????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Oqdaryo")) {//39.839873784601465, 66.73834694617673
//                    sendMessage(user.getChatId().toString(), setApiK(39.839873784601465, 66.73834694617673, "??????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.839873784601465, 66.73834694617673, "??????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Past darg'om")) { // 39.704352292734065, 66.62242053041332
//                    sendMessage(user.getChatId().toString(), setApiK(39.704352292734065, 66.62242053041332, "??????????????????, ???????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.704352292734065, 66.62242053041332, "??????????????????, ???????? ????????????", "???????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Urgut")) {//39.447002526177904, 67.17173665735707
//                    sendMessage(user.getChatId().toString(), setApiK(39.447002526177904, 67.17173665735707, "??????????????????, ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.447002526177904, 67.17173665735707, "??????????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Paxtachi")) {//39.888515759299004, 65.6089318735053
//                    sendMessage(user.getChatId().toString(), setApiK(39.888515759299004, 65.6089318735053, "??????????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.888515759299004, 65.6089318735053, "??????????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Poyariq")) {//40.05104556455984, 66.85419779064077
//                    sendMessage(user.getChatId().toString(), setApiK(40.05104556455984, 66.85419779064077, "??????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.05104556455984, 66.85419779064077, "??????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Samarqand tumani")) { //39.59818617036431, 66.90471762188967
//                    sendMessage(user.getChatId().toString(), setApiK(39.59818617036431, 66.90471762188967, "??????????????????, ?????????????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.59818617036431, 66.90471762188967, "??????????????????, ?????????????????? ????????????", "?????????????????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Toyloq")) { //39.56442359774873, 67.14018598905683
//                    sendMessage(user.getChatId().toString(), setApiK(39.56442359774873, 67.14018598905683, "??????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.56442359774873, 67.14018598905683, "??????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qo'shrabot")) { //40.38655726843597, 66.5100175021684
//                    sendMessage(user.getChatId().toString(), setApiK(40.38655726843597, 66.5100175021684, "??????????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.38655726843597, 66.5100175021684, "??????????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_buxoro)) {
                if (data.equals("Buxoro shahri")) {//39.77672367865153, 64.4210785732946
//                    sendMessage(user.getChatId().toString(), setApiK(39.77672367865153, 64.4210785732946, "????????????, ???????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.77672367865153, 64.4210785732946, "????????????, ???????????? ??????????", "???????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Buxoro tumani")) { //39.63566090876281, 64.30230294221505
//                    sendMessage(user.getChatId().toString(), setApiK(39.63566090876281, 64.30230294221505, "????????????, ???????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.63566090876281, 64.30230294221505, "????????????, ???????????? ????????????", "???????????? ????????????");
//                    sendKunOyKrla(user.getChatId(), data);
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("G'ijduvon")) {//40.601906240839995, 64.71053498588797
//                    sendMessage(user.getChatId().toString(), setApiK(40.601906240839995, 64.71053498588797, "????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.601906240839995, 64.71053498588797, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Jondor")) {//39.971219891973426, 63.5714954269693
//                    sendMessage(user.getChatId().toString(), setApiK(39.971219891973426, 63.5714954269693, "????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.971219891973426, 63.5714954269693, "????????????, ????????????", "????????????");
//                    sendKunOyKrla(user.getChatId(), data);
//                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Kogon shahri")) { //39.72355224686954, 64.55132893267286
//                    sendMessage(user.getChatId().toString(), setApiK(39.72355224686954, 64.55132893267286, "????????????, ?????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.72355224686954, 64.55132893267286, "????????????, ?????????? ??????????", "?????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Kogon tumani")) { //39.69809076622412, 64.53702710674044
//                    sendMessage(user.getChatId().toString(), setApiK(39.69809076622412, 64.53702710674044, "????????????, ?????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.69809076622412, 64.53702710674044, "????????????, ?????????? ????????????", "?????????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Olot")) { //39.281691000170674, 64.03768298578096
//                    sendMessage(user.getChatId().toString(), setApiK(39.281691000170674, 64.03768298578096, "????????????, ????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.281691000170674, 64.03768298578096, "????????????, ????????", "????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Peshku")) {//40.73303621917936, 63.21959281775061
//                    sendMessage(user.getChatId().toString(), setApiK(40.73303621917936, 63.21959281775061, "????????????, ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.73303621917936, 63.21959281775061, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qorako'l")) { //39.936970007976, 62.941145138407805
//                    sendMessage(user.getChatId().toString(), setApiK(39.936970007976, 62.941145138407805, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.936970007976, 62.941145138407805, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qorovulbozor")) {//39.46527348718642, 64.67070586334809
//                    sendMessage(user.getChatId().toString(), setApiK(39.46527348718642, 64.67070586334809, "????????????, ????????????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.46527348718642, 64.67070586334809, "????????????, ????????????????????????", "????????????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Romitan")) { //40.731071302974954, 62.530718928951856
//                    sendMessage(user.getChatId().toString(), setApiK(40.731071302974954, 62.530718928951856, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.731071302974954, 62.530718928951856, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Shofirkon")) { // 40.12540773474785, 64.4827072959460
//                    sendMessage(user.getChatId().toString(), setApiK(40.12540773474785, 64.48270729594604, "????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.12540773474785, 64.48270729594604, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Vobkent")) { //40.15547517789773, 64.53244365200904
//                    sendMessage(user.getChatId().toString(), setApiK(40.15547517789773, 64.53244365200904, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.15547517789773, 64.53244365200904, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_navoiy)) {
                if (data.equals("Navoiy shahri")) {//40.10166507561083, 65.3575344176788
//                    sendMessage(user.getChatId().toString(), setApiK(40.10166507561083, 65.3575344176788, "????????????, ???????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.10166507561083, 65.3575344176788, "????????????, ???????????? ??????????", "???????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Karmana")) {//40.14372322026956, 65.35467198347476
//                    sendMessage(user.getChatId().toString(), setApiK(40.14372322026956, 65.35467198347476, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.14372322026956, 65.35467198347476, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Konimex")) {// 40.277684998973186, 65.14210146382663
//                    sendMessage(user.getChatId().toString(), setApiK(40.277684998973186, 65.14210146382663, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.277684998973186, 65.14210146382663, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Navbahor")) {//40.232621293600054, 65.23269831157204
//                    sendMessage(user.getChatId().toString(), setApiK(40.232621293600054, 65.23269831157204, "????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.232621293600054, 65.23269831157204, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Nurota")) {//40.61096195334514, 65.93817362794783
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.61096195334514, 65.93817362794783, "????????????, ????????????", "????????????");
//                    sendMessage(user.getChatId().toString(), setApiK(40.61096195334514, 65.93817362794783, "????????????, ????????????"));
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Qiziltepa")) {//39.8966432527515, 64.76528114807608
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.8966432527515, 64.76528114807608, "????????????, ??????????????????", "??????????????????");
//                    sendMessage(user.getChatId().toString(), setApiK(39.8966432527515, 64.76528114807608, "????????????, ??????????????????"));
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Tomdi")) {//42.29906147558487, 64.90150727134733
//                    sendMessage(user.getChatId().toString(), setApiK(42.29906147558487, 64.90150727134733, "????????????, ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 42.29906147558487, 64.90150727134733, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
                if (data.equals("Uchquduq")) {//42.44642264030942, 62.6945789709221
//                    sendMessage(user.getChatId().toString(), setApiK(42.44642264030942, 62.6945789709221, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 42.44642264030942, 62.6945789709221, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
                if (data.equals("Xatirchi")) {//40.230069075732, 65.9409253080852
//                    sendMessage(user.getChatId().toString(), setApiK(40.230069075732, 65.9409253080852, "????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.230069075732, 65.9409253080852, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
                if (data.equals("Zarafshon")) {//41.574460147847816, 64.18331570807477
//                    sendMessage(user.getChatId().toString(), setApiK(41.574460147847816, 64.18331570807477, "????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.574460147847816, 64.18331570807477, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_namangan)) {
                if (data.equals("Chust")) { // 41.00615643758215, 71.23771796120751
//                    sendMessage(user.getChatId().toString(), setApiK(40.999931, 71.236137, "????????????????, ????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.999931, 71.236137, "????????????????, ????????", "????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Namangan")) { // Namangan shahar
//                    sendMessage(user.getChatId().toString(), setApiK(41.007162, 71.643712, "????????????????, ???????????????? ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.007162, 71.643712, "????????????????, ???????????????? ??????????", "???????????????? ??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Kosonsoy")) { // Kosonsoy shahar
//                    sendKunOyKrla(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApiK(41.19608642817571, 71.52652970907708, "????????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.19608642817571, 71.52652970907708, "????????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Chortoq")) { // Chortoq shahar 41.06368431981446, 71.8230565658223
//                    sendKunOyKrla(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApiK(41.06368431981446, 71.8230565658223, "????????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.06368431981446, 71.8230565658223, "????????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Norin")) { // Namangan shahar 40.940509509996495, 72.00654567901067
//                    sendMessage(user.getChatId().toString(), setApiK(40.940509509996495, 72.00654567901067, "????????????????, ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.940509509996495, 72.00654567901067, "????????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Mingbuloq")) { // Namangan shahar 40.78917645308457, 71.38875951462421
//                    sendMessage(user.getChatId().toString(), setApiK(40.78917645308457, 71.38875951462421, "????????????????, ??????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.78917645308457, 71.38875951462421, "????????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Pop")) { // Namangan shahar 41.0364117324324, 70.75487013756214
//                    sendMessage(user.getChatId().toString(), setApiK(41.0364117324324, 70.75487013756214, "????????????????, ??????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.0364117324324, 70.75487013756214, "????????????????, ??????", "??????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("To'raqo'rg'on")) { // Namangan shahar 40.99316007632018, 71.51626227933346
//                    sendMessage(user.getChatId().toString(), setApiK(40.99316007632018, 71.51626227933346, "????????????????, ????????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.99316007632018, 71.51626227933346, "????????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Uchqo'rg'on")) { // Namangan shahar 41.12068842248576, 72.08711447153537
//                    sendMessage(user.getChatId().toString(), setApiK(41.12068842248576, 72.08711447153537, "????????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.12068842248576, 72.08711447153537, "????????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("uychi")) { // Namangan shahar 41.06721589284562, 71.92292974977094
//                    sendMessage(user.getChatId().toString(), setApiK(71.92292974977094, 71.92292974977094, "????????????????, ????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 71.92292974977094, 71.92292974977094, "????????????????, ????????", "????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("yangiqorgon")) { // Namangan shahar 41.198968289144275, 71.71703917988339
//                    sendKunOyKrla(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApiK(41.198968289144275, 71.71703917988339, "????????????????, ????????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.198968289144275, 71.71703917988339, "????????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_fargona)) {
                if (data.equals("Farg???ona shahar")) { //40.37949214005235, 71.78153222147638
//                    sendKunOyKrla(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApiK(40.37949214005235, 71.78153222147638, "??????????????, ?????????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.37949214005235, 71.78153222147638, "??????????????, ?????????????? ??????????", "?????????????? ??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Uchko???prik")) { //40.53267491097347, 71.02892566146784
//                    sendMessage(user.getChatId().toString(), setApiK(40.53267491097347, 71.02892566146784, "??????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.53267491097347, 71.02892566146784, "??????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("So???x")) { //39.968927659480165, 71.12908694248515
//                    sendMessage(user.getChatId().toString(), setApiK(39.968927659480165, 71.12908694248515, "??????????????, ??????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.968927659480165, 71.12908694248515, "??????????????, ??????", "??????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Yozyovon")) { //40.62131813678478, 71.6480687836224
//                    sendMessage(user.getChatId().toString(), setApiK(40.62131813678478, 71.6480687836224, "??????????????, ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.62131813678478, 71.6480687836224, "??????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Quva")) { //40.524573367810504, 72.06257627873157
//                    sendKunOyKrla(user.getChatId(), data);
//                    sendMessage(user.getChatId().toString(), setApiK(40.524573367810504, 72.06257627873157, "??????????????, ????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.524573367810504, 72.06257627873157, "??????????????, ????????", "????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Qo???qon")) { //40.524621966621304, 70.93356479162965
//                    sendMessage(user.getChatId().toString(), setApiK(40.524621966621304, 70.93356479162965, "??????????????, ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.524621966621304, 70.93356479162965, "??????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Beshariq")) { //40.39690886265036, 70.57359975866977
//                    sendMessage(user.getChatId().toString(), setApiK(40.39690886265036, 70.57359975866977, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.39690886265036, 70.57359975866977, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Quvasoy")) { //
//                    sendMessage(user.getChatId().toString(), setApiK(40.30222, 71.97444, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.30222, 71.97444, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Farg???ona")) { //40.320714145540286, 71.70209565835899
//                    sendMessage(user.getChatId().toString(), setApiK(40.320714145540286, 71.70209565835899, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.320714145540286, 71.70209565835899, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Toshloq")) { //40.56269866588281, 71.84044642257277
//                    sendMessage(user.getChatId().toString(), setApiK(40.56269866588281, 71.84044642257277, "??????????????, ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.56269866588281, 71.84044642257277, "??????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("O???zbekiston")) { //40.330297796679396, 70.85868054864669
//                    sendMessage(user.getChatId().toString(), setApiK(40.330297796679396, 70.85868054864669, "??????????????, ????????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.330297796679396, 70.85868054864669, "??????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Qo???shtepa")) { //40.53692345413236, 71.6415020647065
//                    sendMessage(user.getChatId().toString(), setApiK(40.53692345413236, 71.6415020647065, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.53692345413236, 71.6415020647065, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Marg???ilon")) { //40.47635685369382, 71.71252435127899
//                    sendMessage(user.getChatId().toString(), setApiK(40.47635685369382, 71.71252435127899, "??????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.47635685369382, 71.71252435127899, "??????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Buvayda")) { //40.645693110093404, 71.10204505839337
//                    sendMessage(user.getChatId().toString(), setApiK(40.645693110093404, 71.10204505839337, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.645693110093404, 71.10204505839337, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Dang???ara")) { //40.58139710281787, 70.91477380795894
//                    sendMessage(user.getChatId().toString(), setApiK(40.58139710281787, 70.91477380795894, "??????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.58139710281787, 70.91477380795894, "??????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Oltiariq")) { //40.39370084174377, 71.48670380912914
//                    sendMessage(user.getChatId().toString(), setApiK(40.39370084174377, 71.48670380912914, "??????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.39370084174377, 71.48670380912914, "??????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Rishton")) { //40.36410679785719, 71.27208639645364
//                    sendMessage(user.getChatId().toString(), setApiK(40.36410679785719, 71.27208639645364, "??????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.36410679785719, 71.27208639645364, "??????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Furqat")) { //40.51673390388502, 70.71177286893743
//                    sendMessage(user.getChatId().toString(), setApiK(40.51673390388502, 70.71177286893743, "??????????????, ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.51673390388502, 70.71177286893743, "??????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Bag???dod")) { //40.49387968871484, 71.20366862141532
//                    sendMessage(user.getChatId().toString(), setApiK(40.49387968871484, 71.20366862141532, "??????????????, ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.49387968871484, 71.20366862141532, "??????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_jizzah)) {
                if (data.equals("Jizzax shahri")) {//40.11977007458388, 67.85232343283624
//                    sendMessage(user.getChatId().toString(), setApiK(40.11977007458388, 67.85232343283624, "????????????, ???????????? ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.11977007458388, 67.85232343283624, "????????????, ???????????? ??????????", "???????????? ??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Arnasoy")) { //40.59901232957254, 67.79358090175431
//                    sendMessage(user.getChatId().toString(), setApiK(40.59901232957254, 67.79358090175431, "????????????, A????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.59901232957254, 67.79358090175431, "????????????, A????????????", "A????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Baxmal")) {//39.78225382213002, 67.7250016175458
//                    sendMessage(user.getChatId().toString(), setApiK(39.78225382213002, 67.7250016175458, "????????????, ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.78225382213002, 67.7250016175458, "????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Forish")) {//40.70901562393393, 67.18181520202268
//                    sendMessage(user.getChatId().toString(), setApiK(40.70901562393393, 67.18181520202268, "????????????, ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.70901562393393, 67.18181520202268, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("G'allaorol")) { //40.10205495202923, 67.32846413281408
//                    sendMessage(user.getChatId().toString(), setApiK(40.10205495202923, 67.32846413281408, "????????????, ??????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.10205495202923, 67.32846413281408, "????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Do'stlik")) { //40.58181976770775, 68.04548305263552
//                    sendMessage(user.getChatId().toString(), setApiK(40.5818197677077, 68.04548305263552, "????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.5818197677077, 68.04548305263552, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Jizzax tumani")) { //40.11455358141378, 67.82473708142044
//                    sendMessage(user.getChatId().toString(), setApiK(40.11455358141378, 67.82473708142044, "????????????, ???????????? ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.11455358141378, 67.82473708142044, "????????????, ???????????? ????????????", "???????????? ????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Mirzacho'l")) {//40.724412215707, 68.06118184566003
//                    sendMessage(user.getChatId().toString(), setApiK(40.724412215707, 68.06118184566003, "????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.724412215707, 68.06118184566003, "????????????, ????????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Paxtakor")) { //40.35950221136383, 68.00475878330718
//                    sendMessage(user.getChatId().toString(), setApiK(40.35950221136383, 68.00475878330718, "????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.35950221136383, 68.00475878330718, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Yangiobod")) {//39.99243484998208, 68.74527110184545
//                    sendMessage(user.getChatId().toString(), setApiK(39.99243484998208, 68.74527110184545, "????????????, ????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.99243484998208, 68.74527110184545, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Zafarobod")) { //40.41330581212753, 67.76451866402623
//                    sendMessage(user.getChatId().toString(), setApiK(40.41330581212753, 67.76451866402623, "????????????, ??????????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.41330581212753, 67.76451866402623, "????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Zarband")) { //40.189983986463055, 68.11455808514548
//                    sendMessage(user.getChatId().toString(), setApiK(40.189983986463055, 68.11455808514548, "????????????, ??????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 40.189983986463055, 68.11455808514548, "????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Zomin")) { //39.88553183671697, 68.3643022291486
//                    sendMessage(user.getChatId().toString(), setApiK(39.88553183671697, 68.3643022291486, "????????????, ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.88553183671697, 68.3643022291486, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
                }
            } else if (user.getStep().equals(UserStep.select_lang_krl_xorazm)) {
                if (data.equals("Bog'ot")) {//41.33744153309746, 60.87694940015311
//                    sendMessage(user.getChatId().toString(), setApiK(41.33744153309746, 60.87694940015311, "????????????, ??????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.33744153309746, 60.87694940015311, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Gurlan")) {//41.90536583933732, 60.37982747138814
//                    sendMessage(user.getChatId().toString(), setApiK(41.90536583933732, 60.37982747138814, "????????????, ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.90536583933732, 60.37982747138814, "????????????, ????????????", "????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Shovot")) {//41.710201238586826, 60.27012648475865
//                    sendMessage(user.getChatId().toString(), setApiK(41.710201238586826, 60.27012648475865, "????????????, ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.710201238586826, 60.27012648475865, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qo'shko'pir")) {//41.526982196908484, 60.269577660413084
//                    sendMessage(user.getChatId().toString(), setApiK(41.526982196908484, 60.269577660413084, "????????????, ???????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.526982196908484, 60.269577660413084, "????????????, ???????????????? ", "????????????????");
//                    sendKunOyKrla(user.getChatId(), data);
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Yangibozor")) {// 41.75025881608148, 60.54049313655263
//                    sendMessage(user.getChatId().toString(), setApiK(41.75025881608148, 60.54049313655263, "????????????, ?????????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.75025881608148, 60.54049313655263, "????????????, ?????????????????? ", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Urganch shahri")) {//41.547992018041974, 60.618264246685676
//                    sendMessage(user.getChatId().toString(), setApiK(41.547992018041974, 60.618264246685676, "????????????, ???????????? ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.547992018041974, 60.618264246685676, "????????????, ???????????? ??????????", "???????????? ??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Urganch tumani")) {//41.61625437893366, 60.533593007029786
//                    sendMessage(user.getChatId().toString(), setApiK(41.61625437893366, 60.533593007029786, "????????????, ???????????? ????????????"));
//                    sendKunOyKrla(user.getChatId(), data);
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.61625437893366, 60.533593007029786, "????????????, ???????????? ????????????", "???????????? ????????????");
                    userService.updateData(user, user.getId(), data);
                } else if (data.equals("Xazorasp")) {//41.30448683950074, 61.09745692690238
//                    sendMessage(user.getChatId().toString(), setApiK(41.30448683950074, 61.09745692690238, "????????????, ???????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.30448683950074, 61.09745692690238, "????????????, ???????????????? ", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Xiva")) {//41.39184570391674, 60.34725590732044
//                    sendMessage(user.getChatId().toString(), setApiK(41.39184570391674, 60.34725590732044, "????????????, ???????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.39184570391674, 60.34725590732044, "????????????, ???????? ", "????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Xonqa")) {//41.481393767464844, 60.829570881655584
//                    sendMessage(user.getChatId().toString(), setApiK(41.481393767464844, 60.829570881655584, "????????????, ??????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.481393767464844, 60.829570881655584, "????????????, ??????????", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Yangiariq")) {// 41.346206916399666, 60.54804614851794
//                    sendMessage(user.getChatId().toString(), setApiK(41.346206916399666, 60.54804614851794, "????????????, ????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 41.346206916399666, 60.54804614851794, "????????????, ????????????????", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }

            } else if (user.getStep().equals(UserStep.select_lang_krl_qashqadaryo)) {
                if (data.equals("Qarshi shahri")) {//38.86191354410989, 65.78491933285783
//                    sendMessage(user.getChatId().toString(), setApiK(38.86191354410989, 65.78491933285783, "??????????????????, ?????????? ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.86191354410989, 65.78491933285783, "??????????????????, ?????????? ?????????? ", "?????????? ?????????? ");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qarshi tumani")) {//38.779267614177556, 65.75278333536036
//                    sendMessage(user.getChatId().toString(), setApiK(38.779267614177556, 65.75278333536036, "??????????????????, ?????????? ????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.779267614177556, 65.75278333536036, "??????????????????, ?????????? ????????????", "?????????? ????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Chiroqchi")) {//39.18112111196543, 66.25891591391694
//                    sendMessage(user.getChatId().toString(), setApiK(39.18112111196543, 66.25891591391694, "??????????????????, ?????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.18112111196543, 66.25891591391694, "??????????????????, ?????????????? ", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Dehqonobod")) {//38.35730703486065, 66.47533103897435
//                    sendMessage(user.getChatId().toString(), setApiK(38.35730703486065, 66.47533103897435, "??????????????????, ????????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.35730703486065, 66.47533103897435, "??????????????????, ????????????????????", "????????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("G'uzor")) {//38.587010422831746, 66.04489530163235
//                    sendMessage(user.getChatId().toString(), setApiK(38.587010422831746, 66.04489530163235, "??????????????????, ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.587010422831746, 66.04489530163235, "??????????????????, ?????????? ", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Kasbi")) {//38.93540618287719, 65.43383657129233
//                    sendMessage(user.getChatId().toString(), setApiK(38.93540618287719, 65.43383657129233, "??????????????????, ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.93540618287719, 65.43383657129233, "??????????????????, ?????????? ", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Kitob")) {// 39.2152414606226, 67.03598426370185
//                    sendMessage(user.getChatId().toString(), setApiK(39.2152414606226, 67.03598426370185, "??????????????????, ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.2152414606226, 67.03598426370185, "??????????????????, ?????????? ", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Koson")) {// 39.16770611275984, 65.77183802497865
//                    sendMessage(user.getChatId().toString(), setApiK(39.16770611275984, 65.77183802497865, "??????????????????, ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.16770611275984, 65.77183802497865, "??????????????????, ?????????? ", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Mirishkor")) {// 38.874194821533756, 64.92111490671182
//                    sendMessage(user.getChatId().toString(), setApiK(38.874194821533756, 64.92111490671182, "??????????????????, ???????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.874194821533756, 64.92111490671182, "??????????????????, ???????????????? ", "????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Muborak")) {//39.30420398425692, 65.25997593377225
//                    sendMessage(user.getChatId().toString(), setApiK(39.30420398425692, 65.25997593377225, "??????????????????, ?????????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 39.30420398425692, 65.25997593377225, "??????????????????, ?????????????? ", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Nishon")) {//38.53608390327857, 65.48400423357076
//                    sendMessage(user.getChatId().toString(), setApiK(38.53608390327857, 65.48400423357076, "??????????????????, ?????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.53608390327857, 65.48400423357076, "??????????????????, ?????????? ", "??????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Qamashi")) {// 38.75660026948466, 66.60185133772104
//                    sendMessage(user.getChatId().toString(), setApiK(38.75660026948466, 66.60185133772104, "??????????????????, ???????????? "));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.75660026948466, 66.60185133772104, "??????????????????, ???????????? ", "????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Shahrisabz")) {//38.99171024720661, 67.2009187750465
//                    sendMessage(user.getChatId().toString(), setApiK(38.99171024720661, 67.2009187750465, "??????????????????, ??????????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.99171024720661, 67.2009187750465, "??????????????????, ??????????????????", "??????????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                } else if (data.equals("Yakkabog'")) {//38.90742740574835, 66.75665915180723
//                    sendMessage(user.getChatId().toString(), setApiK(38.90742740574835, 66.75665915180723, "??????????????????, ??????????????"));
                    buttongaVatBnJonatishKrl(user.getChatId(), 38.90742740574835, 66.75665915180723, "??????????????????, ??????????????", "??????????????");
                    userService.updateData(user, user.getId(), data);
//                    sendKunOyKrla(user.getChatId(), data);
                }

            }
//            deleteInlineKeyboard(user.getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId().toString());
        }

    }

    private void deleteInlineKeyboard(String chatID, String inlineMessageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(Integer.valueOf(inlineMessageId));
        deleteMessage.setChatId(chatID);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    private SendMessage sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return sendMessage;
    }

    public void sendInlineBtn(Long chatId) {
        try {
            execute(btn.sendInlineKeyBoardMessage(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendRegionBtn(Long chatId) {
        try {
            execute(regionBtn.getRegionBtn(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void namanganTumans(Long chatId) {
        try {
            execute(namangan.namanganTumans(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void namanganTumansK(Long chatId) {
        try {
            execute(namangan.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setJizzahK(Long chatId) {
        try {
            execute(jizzah.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void setXorazmK(Long chatId) {
        try {
            execute(xorazm.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void getToshkentKrl(Long chatId) {
        try {
            execute(toshkent.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBuxoroK(Long chatId) {
        try {
            execute(buxoro.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void getNamvoiyK(Long chatId) {
        try {
            execute(navoiy.navoiyTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void getsamarqandKrl(Long chatId) {
        try {
            execute(samarqand.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void getQashqa(Long chatId) {
        try {
            execute(qashqadaryo.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void andijonTumans(Long chatId) {
        try {
            execute(andijon.namanganTumans(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void fagonaTumans(Long chatId) {
        try {
            execute(fagona.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void andijonTumansK(Long chatId) {
        try {
            execute(andijon.namanganTumansK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public String setApi(Double lat, Double lng, String text) {
        return prayTime.main(lat, lng, text);
    }

    public String setApiK(Double lat, Double lng, String text) {
        return prayTime.mainK(lat, lng, text, "");
    }

    public void sendKunOy(Long chatId, String data) {
        try {
            execute(regionBtn.kunOy(chatId, data));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void MuvaffaqiyatliKrl(Long chatId, String data) {
        try {
            execute(regionBtn.MuvaffaqiyatliKrl(chatId, data));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void MuvaffaqiyatliLt(Long chatId, String data) {
        try {
            execute(regionBtn.MuvaffaqiyatliLt(chatId, data));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private void getRegionInline(Long id) {
        try {
            execute(btn.sendInlineKeyBoardRegion(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineKeyBoardRegion(Long id) throws TelegramApiException {
        execute(namangan.namanganTumans(id));
    }

    private void sendInlineKeyBoardRegionK(Long id) {
        try {
            execute(btn.sendInlineKeyBoardRegionK(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineToshknet(Long id) {
        try {
            execute(toshkent.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void getRegionBtnKrl(Long id) {
        try {
            execute(regionBtn.getRegionBtnKrl(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineQashqadaryo(Long id) {
        try {
            execute(qashqadaryo.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineFargona(Long id) {
        try {
            execute(fagona.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineBuxoro(Long id) {
        try {
            execute(buxoro.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineNavoiy(Long id) {
        try {
            execute(navoiy.navoiyTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineXorazm(Long id) {
        try {
            execute(xorazm.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineSamarqand(Long id) {
        try {
            execute(samarqand.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineJizzah(Long id) {
        try {
            execute(jizzah.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendInlineSirdaryo(Long id) {
        try {
            execute(sirdaryo.namanganTumans(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void tilniOzgartirish(Long id) {
        try {
            execute(btn.tilniOzgartirish(id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void buttongaVatBnJonatish(Long chatId, Double lat, Double lang, String text, String data) {
        try {
            execute(regionBtn.buttongaVatBnJonatish(chatId, lat, lang, text, data));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void buttongaVatBnJonatishKrl(Long chatId, Double lat, Double lang, String text, String data) {
        try {
            execute(regionBtn.buttongaVatBnJonatishKrl(chatId, lat, lang, text, data));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void kllLt(Long chatId) {
        try {
            execute(regionBtn.kllLt(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void locationBTN(Long chatId) {
        try {
            execute(regionBtn.locationBTN(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }  public void locationBTNK(Long chatId) {
        try {
            execute(regionBtn.locationBTNK(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void ltkll(Long chatId) {
        try {
            execute(regionBtn.lTkll(chatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyRemove(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sendMessage.setText("???");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
 private void replyRemove_1(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sendMessage.setText("Biroz kuting...");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteInlineKeyboard(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteInlineKeyboard(Message message, Integer id) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(id);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteInlineKeyboard1(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId() + 1);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    } public void deleteInlineKeyboard_1(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId() - 1);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}