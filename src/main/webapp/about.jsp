<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<t:genericpage>
    <jsp:body>
      <div class="content">
        <div class="row">
        <c:choose>
            <c:when test="${order == 1}">
                <div class="col-lg-6">
                    <img class="rounded mx-auto d-block" width="50%" src="/resources/img/paul.jpg">
                    <div class="text-center">
                        <h3 class="font-weight-bold">Paul</h3>
                        <p>I'm a <b>STEP Intern</b> in the Plx ZRH team. I'm from Romania, a country from the Southestern part of Europe.</br>
                            I'm studying Computer Science at Babes-Bolyai University in Cluj-Napoca.<br> 
                            In my spare time I playing on my <b>Nintendo Switch</b> and I enjoy <b>swimming</b>.</br>
                            I'm in love with the <b>Marvel</b>Universe</b>! One day I'll have a big comics collection.
                        </p>
                        <a href="https://contar.io/stefanut999" target="_blank">Contact</a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <img class="rounded mx-auto d-block" width="50%" src="/resources/img/beatrice.jpg">
                    <div class="text-center">
                        <h3 class="font-weight-bold">Beatrice</h3>
                        <p> I am a <b>STEP Intern</b> in the Plx ZRH
                        team. I was born and raised in <b>Italy</b> and I’ve been living in
                        the UK for the past 5 years. </br>
                        Currently, I am a 2nd year undergrad reading
                        <b>Computer Science and Philosophy at Oxford University</b> (so if you
                        have any deep philosophical questions fire away!).</br>
                        In my spare time I enjoy
                        <b>horse riding, hiking, playing board games and baking</b> (chocolate
                        cake anyone?). This is my first work experience and I'm so excited to
                        be working for Google!</p>
                        <a href="" target="_blank">Contact</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                    <div class="col-lg-6">
                        <img class="rounded mx-auto d-block" width="50%" src="/resources/img/beatrice.jpg">
                        <div class="text-center">
                            <h3 class="font-weight-bold">Beatrice</h3>
                            <p> I am a <b>STEP Intern</b> in the Plx ZRH
                            team. I was born and raised in <b>Italy</b> and I’ve been living in
                            the UK for the past 5 years.</br>
                            Currently, I am a 2nd year undergrad reading
                            <b>Computer Science and Philosophy at Oxford University</b> (so if you
                            have any deep philosophical questions fire away!).</br>
                            In my spare time I enjoy
                            <b>horse riding, hiking, playing board games and baking</b> (chocolate
                            cake anyone?). This is my first work experience and I'm so excited to
                            be working for Google!</p>
                            <a href="" target="_blank">Contact</a>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <img class="rounded mx-auto d-block" width="50%" src="/resources/img/paul.jpg">
                        <div class="text-center">
                            <h3 class="font-weight-bold">Paul</h3>
                            <p>I'm a <b>STEP Intern</b> in the Plx ZRH team. I'm from Romania, a country from the Southestern part of Europe.</br>
                                I'm studying Computer Science at Babes-Bolyai University in Cluj-Napoca.<br> 
                                In my spare time I playing on my <b>Nintendo Switch</b> and I enjoy <b>swimming</b>.</br>
                                I'm in love with the <b>Marvel</b> Universe! One day I'll have a big comics collection.
                            </p>                            
                            <a href="https://contar.io/stefanut999" target="_blank">Contact</a>
                        </div>
                    </div>
             </c:otherwise>       
        </c:choose>
        </div>
      </div>
    </jsp:body>
</t:genericpage>
