package com.icecream.cc.bean;

/**
 * Created by 陈湘龙 on 2020/5/16.
 */
public class BasicWordsInDB {
    private String[] names = new String[]{
            "hello",
            "encouragement",
            "quantify",
            "enclose",
            "complexion",
            "mortgage",
            "perpetual",
            "cozy",
            "at length",
            "come up with",
            "live on",
            "exploit",
            "feast",
            "inspection",
            "deliver",
            "talk into"
    };
    private String[] IPA = new String[]{
            "[həˈləʊ]",
            "[ɪnˈkʌrɪdʒmənt]",
            "[ˈkwɒntɪfaɪ]",
            "[ɪnˈkləʊzɪz]",
            "[kəmˈplekʃn]",
            "[ˈmɔːɡɪdʒ]",
            "[pəˈpetʃuəl]",
            "[ˈkəʊzi]",
            "[æt leŋθ]",
            "[kʌm ʌp wɪð]",
            "[lɪv ɒn]",
            "[ɪkˈsplɔɪt]",
            "[fiːst]",
            "[ɪnˈspekʃn]",
            "[dɪˈlɪvə(r)]",
            "[tɔːk ˈɪntə]"
    };
    private String[] meaning = new String[]{
            "n.哈罗，喂，你好;",
            "n.鼓舞;鼓励;",
            "v.以数量表述;量化",
            "v.(用墙、篱笆等)把…围起来;围住;",
            "n.面色;肤色;气色;(事物的)性质，特性",
            "n.按揭(由银行等提供房产抵押借款);v.(向银行等)抵押(房产)",
            "adj.不间断的;持续的;长久的;",
            "adj.温暖舒适的;n.保温罩;v.蒙骗；抚慰",
            "终究;终归;最终;长久地;详尽地",
            "想出，提出(计划、想法等)",
            "靠（某一数目的钱）过活;以…为食;",
            "v.剥削;压榨;运用;n.英勇(或激动人心、引人注目)的行为",
            "n.盛宴;宴会;(宗教的)节日;v.尽情享用(美味佳肴)",
            "n.视察;检查;查看;审视",
            "v.递送;传送;交付;运载;发表;宣布;",
            "说服，劝服（尤指做错事或蠢事）"
    };
    private String[] example = new String[]{
            "Hello,Icecream!\n你好，Icecream！",
            "a few words of encouragement\n几句鼓励的话",
            "The risks to health are impossible to quantify.\n健康的风险是无法用数量表示的。",
            "The rules state that samples must be enclosed in two watertight containers.\n规则要求样本必须装在两个水密容器中。",
            "a move which changed the political complexion of the country\n改变国家政局的举措",
            "He had to mortgage his house to pay his legal costs.\n他不得不把房子抵押出去来付诉讼费。",
            "How can I work with these perpetual interruptions?\n打扰不断，让我怎么工作？",
            "I have a cozy family.\n我有一个舒适的家。",
            "At length my father went into the house.\n我父亲最后还是进了屋。",
            "If Warren can come up with the $15 million, we'll go to London.\n如果沃伦能拿出那1500万美元，我们就去伦敦。",
            " The southerners in China mainly live on rice.\n中国南方的人主要以大米为食。",
            "He exploited his father's name to get himself a job.\n他利用他父亲的名声为自己找到一份工作。",
            "The evening was a real feast for music lovers.\n这个晚会真是让音乐爱好者大饱耳福。",
            "Engineers carried out a thorough inspection of the track.\n工程师对轨道进行了彻底检查。",
            "Leaflets have been delivered to every household.\n传单已发送到每家每户。",
            "He talked me into marrying him. He also talked me into having a baby...\n他花言巧语让我嫁给了他，又说服我生了个孩子。"
    };

    public String[] getNames() {
        return names;
    }

    public String[] getIPA() {
        return IPA;
    }

    public String[] getMeaning() {
        return meaning;
    }

    public String[] getExample() {
        return example;
    }
}
