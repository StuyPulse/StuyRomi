package com.stuypulse.robot.commands;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

// TODO: Find out what we can use from here
//      - IFilter: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/IFilter.html
//      - All of the filters: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/package-summary.html
import com.stuypulse.stuylib.streams.filters.*;

public class DrivetrainDriveCommand extends CommandBase {
    
    private final Drivetrain drivetrain; 
    private final Gamepad gamepad;
    
    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
    }
    
    IFilter filter = new IFilterGroup(new LowPassFilter(0.5));

    
    public void execute() {

        double speed = gamepad.getRightTrigger() - gamepad.getLeftTrigger();
        double turn = gamepad.getLeftStick().x;

        // TODO: Filter these values sending them to the drivetrain
        
        speed = filter.get(speed); 
        
        drivetrain.arcadeDrive(speed, turn);
        
        turn = filter.get(turn);
    } 
}
/*  
We're no strangers to love
You know the rules and so do I
A full commitment's what I'm thinking of
You wouldn't get this from any other guy

I just wanna tell you how I'm feeling
Gotta make you understand

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

We've known each other for so long
Your heart's been aching but you're too shy to say it
Inside we both know what's been going on
We know the game and we're gonna play it

And if you ask me how I'm feeling
Don't tell me you're too blind to see

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you

(Ooh give you up)
(Ooh give you up)
(Ooh) Never gonna give, never gonna give
(Give you up)
(Ooh) Never gonna give, never gonna give
(Give you up)

We've known each other for so long
Your heart's been aching but you're too shy to say it
Inside we both know what's been going on
We know the game and we're gonna play it

I just wanna tell you how I'm feeling
Gotta make you understand

Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry
Never gonna say goodbye
Never gonna tell a lie and hurt you
Never gonna give you up
Never gonna let you down
Never gonna run around and desert you
Never gonna make you cry

 Why not call it the Big Chill or the Nippy era?

          I'm just sayin', how do we know it's an ice age?

            Because of all the ice.

            Well, things just got a little chillier.

            Help. Help.

            Come on, kids, let's go. The traffic's movin'.

            But, but, but, Dad.

            No buts. You can play extinction later.

            OK. Come on, guys.

            So, where's Eddie?

            He said he was on the verge of an evolutionary breakthrough.

            Really?

            I'm flying.

            Some breakthrough.

            - Look out. - You're going the wrong way.

            Crazy mammoth.

            Do the world a favor. Move your issues off the road.

            If my trunk was that small, I wouldn't draw attention to myself, pal.

            Give me a break. We've been waddlin' all day.

            Go ahead. Follow the crowd.

            It'll be quieter when you're gone.

            Come on. If he wants to freeze to death, let him.

            I'm up. I'm up.

            Rise and shine, everybody. Huh? Zak? Marshall?

            Bertie? Uncle Fungus?

            Where is everybody? Come on, guys, we're gonna miss the migration.

            They left without me. They do this every year.

            Why? Doesn't anyone love me?

            Isn't there anyone who cares about Sid the Sloth?

            All right, I'll just go by myself.

            Sick.

            Wide body, curb it next time.

            Oh, jeez. Yuck.

            I can't believe it. Fresh wild greens. Frank, where did you ever...?

            - Go ahead. Dig in. - A dandelion.

            - I thought the frost wiped 'em all out. - All but one.

            It makes me so... I wanna... Yuck.

            This has definitely not been my day. You know what I'm sayin', buddy?

            What a mess. You rhinos have tiny brains. Did you know that?

            It's just a fact. No offense. You probably didn't even know what I'm talkin' about.

            Yummo.

             A dandelion. Must be the last one of the season.

              - Carl. - Easy, Frank.

              He ruined our salad.

              My mistake. That was my mistake. Let me...

              No, no, seriously, let me take care of this. What is this?

              Pine cones. Oh, my goodness. They're my favorite.

              Delicious. That's good eating.

              But don't let me hog them all up. Here, you have some.

              Tasty, isn't it? "Bon appétit."

              Now?

              Now.

              - Just pretend that I'm not here. - I wanted to hit him at full speed.

              - That's OK. We'll have some fun with him. - Don't let them impale me. I wanna live.

              - Get off me. - Come on, you're makin' a scene.

              We'll just take our furry piòata and go.

              If it's not them today, it's someone else tomorrow.

              Well, I'd rather it not be today. OK?

              We'll break your neck so you don't feel a thing.

              Wait a minute. I thought rhinos were vegetarians.

              - An excellent point. - Shut up.

              Who says we're gonna eat him after we kill him?

              I don't like animals that kill for pleasure.

              - Save it for a mammal that cares. - I'm a mammal that cares.

              OK, if either of you make it across that sinkhole in front of ya, you get the sloth.

              That's right, you losers. You take one step and you're dead.

              - You were bluffing, huh? - Yeah. Yeah, that was a bluff.

              Get him.

              A dandelion?

              We did it.

              - You have beautiful eyes. - Get off my face.

              Whoa, we make a great team. What do you say we head south together?

              Great. Jump on my back and relax the whole way.

              - Wow, really? - No.

              Wait, aren't you going south? The change of seasons, migration instincts.

              - Any of this a-ringin' a bell? - I guess not. Bye.

              OK, then. Thanks for the help. I can take it from here.

              You overgrown weasel. Wait till we get down there.

              That south thing is way overrated. The heat, the crowds - who needs it?

              Isn't this great? You and me, two bachelors knockin' about in the wild.

              No, you just want a bodyguard so you don't become somebody's side dish.

              You're a very shrewd mammal.

              OK, lead the way, Mr Big... Didn't get the name.

              - Manfred. - Manfred? Yuck.

              How about Manny the Moody Mammoth? Or Manny the Melancholy... Manny the...

              Stop following me.

              OK, so you've got issues. You won't even know I'm here. I'll just zip the lip.

              Look at the cute little baby, Diego.

              - Isn't it nice he'll be joining us for breakfast? - It wouldn't be breakfast without him.

              Especially since his daddy wiped out half our pack and wears our skin to keep warm.

              An eye for an eye, don't you think?

              Let's show him what happens when he messes with sabers.

              Alert the troops. We attack at dawn.

              And, Diego, bring me that baby alive.

              If I'm gonna enjoy my revenge, I want it to be fresh.

              Phew. I'm wiped out.

              - That's your shelter? - You're a big guy. You got a lotta wood.

              - I'm a little guy. - You got half a stick.

              But with my little stick and my highly evolved brain, I shall create fire.

              Fascinating.

              We'll see if brains triumph over brawn tonight. Now, won't we?

              Think I saw a spark.

              Any chance I could squeeze in there with you, Manny, ol' pal?

              Isn't there someone else you can annoy?

              Friends? Family? Poisonous reptiles?

              My family abandoned me. They just kinda migrated without me.

              You should see what they did last year.

              They woke up early and tied my hands and feet

              and they gagged me with a field mouse,

              covered their tracks, went through water so I'd lose their scent,

              and... who needs 'em, anyway?

              So what about you? You have family?

              OK, you're tired. I see. We'll talk more in the morning.

              Manfred? Manfred? Could you scooch over a drop?

              Come on. Nobody falls asleep that fast.

              Manny.

              There's Diego. Fall back.

              - Where's the baby? - I lost it over the falls.

              You lost it?

              - I want that baby, Diego. - I'll get it.

              You'd better, unless you want to serve as a replacement.

              We'll go up to Half Peak. Meet us there.

              It had better be alive.

              Can we trust you with that, Diego?

              Let's go.

              She picked a hair off my shoulder and says, "If you have an extra mating dance,

              at least pick a female with the same color pelt."

              I thought "Whoa. She's gonna go praying mantis on me."

              If you find a mate, you should be loyal. In your case, grateful.

              - Now get away from me. - I think mating for life is stupid.

              There's plenty of Sid to go around.

              Manny?

              Manny?

              Look at that. He's OK.

              She's gone.

              - Manny, are you forgetting something? - No.

              - But you just saved him. - I'm trying to get rid of the last thing I saved.

              But you can't leave him here.

              Look, there's smoke. That's his herd right up the hill.

              - We should return him. - Let's get this straight. There is no "we".

              There never was a "we". In fact, without me, there wouldn't even be a "you".

              - Just up the hill. - Listen very carefully: I'm not going.

              - Fine, be ajerk. I'll take care of him. - Yeah, that's good.

              You can't even take care of yourself. This, I gotta see.

              I'll return you. We don't need that meany-weeny mammoth, do we?

              No, we don't.

              You're an embarrassment to nature. Do you know that?

              This is cake. I'm fine, I'm fine.

              I'm gonna die.

              Manny.

              - That pink thing is mine. - No. Actually, that pink thing belongs to us.

              "Us"? You two are a bit of an odd couple.

              There is no "us".

              I see. Can't have one of your own, so you want to adopt.

              Look, I'm sorry to interrupt your snack, but we gotta go.

              The baby? Please. I was returning him to his herd.

              Oh, yeah. Nice try, bucktooth.

              - Callin' me a liar? - I didn't say that.

              You were thinkin' it.

              I don't like this cat. He reads minds.

              - Name's Diego, friend. - Manfred, and I'm not your friend.

              Fine, Manfred. If you're lookin' for the humans, you're wastin' your time. They left.

              Thanks for the advice. Now beat it.

              I'll help you bring it to its herd, but leave me alone after that.

              - OK. OK, deal. What's your problem? - You are my problem.

              I think you're stressed, so you eat too much. It's hard to get fat on a vegan diet.

              I'm not fat. It's all this fur. It makes me look poofy.

              All right, you have fat hair. But when you're ready to talk, I'm here.

              What are you doin'? Just drop it on the ledge.

              - We should make sure they found him. - Good idea.

              No, no. Wait, wait, wait, wait.

              Don't spear me.

              - This is a problem. - Now what?

              That's perfect.

              - I told you they were gone. - Look who it is.

              Don't you have some poor animal to disembowel?

              They couldn't be far. I mean, they went this way, or this way?

              You don't know much about tracking, do you?

              I'm a sloth. I see a tree, eat a leaf. That's my tracking.

              You didn't miss them by much.

              It's still green. They headed north two hours ago.

              It's still green. They headed north two hours ago.

              You don't need this aggravation.

              Give me the baby. I can track humans down a lot faster than you.

              And you're just a good citizen helpin' out?

              - I just know where the humans are going. - Glacier Pass.

              Everybody knows they have a settlement on the other side.

              Unless you know how to track, you'll never reach them before snow closes the pass.

              Which should be, like, tomorrow.

              So, you can give that baby to me, or go get lost in a blizzard. It's your choice.

              Here's your little bundle of joy. We're returning it to the humans.

              The big, bad tigey-wigey gets left behind. Poor tigey-wigey.

              Sid, tigey-wigey is gonna lead the way.

              Manny, can I talk to you for a second?

              No. The sooner we find the humans, the sooner I get rid of Mr Stinky Drool-Face,

              and the baby, too.

              You won't always have Jumbo around to protect you.

              And when that day comes, I suggest you watch your back, cos I'll be chewin' on it.

              Über-tracker. Up front, where I can see you.

              Help me.

              You gotta make it stop. I can't take it anymore.

              - I've eaten things that complained less. - He won't stop squirming.

              - Watch its head. - Put it down.

              Jeez, "pick him up, put him down..."

              - Its nose is dry. - That means something's wrong with it.

              - Someone should lick it. Just in case. - I'll do it.

              - He's wearing one of those baby thingies. - So?

              So if he poops, where does it go?

              - Humans are disgusting. - OK, you. Check for poop.

              - Why am I the poop-checker? - Returning him was your idea,

              you're small and insignificant, and I'll pummel you if you don't.

              - Why else? - Now, Sid.

              I mean, my goodness. Look out. Coming through.

              - Watch out. - Stop wavin' that thing around.

              I'm gonna slip.

              It's clean. Got ya!

              Will you cut it out?

              Do that again. He likes it.

              It's makin' me feel better too.

              Here, you hold it.

              Turn him towards me.

              Where is the baby?

              There he is.

              Where is the baby?

              There he is.

              Stop it. You're scarin' him.

              - I bet he's hungry. - How about some milk?

              - I'd love some. - Not you. The baby.

              I ain't exactly lactating right now, pal.

              - You're a little low on the food chain to... - Enough.

              Enough, enough, enough.

              Food.

              I don't know, but I've been told

              I don't know, but I've been told

              End of the world be mighty cold

              End of the world be mighty cold

              Prepare for the Ice age!

              Protect the dodo way of life!

              Survival separates the dodos from the beasts!

              Protect the dodo way of life!

              Prepare for the Ice age!

              - Ice age? - I've heard of these crackpots.

              - Intruders. - Now, don't fall in.

              - If you do, you will definitely... - Intruders. Intruder...

              ...burn and die.

              Can we have our melon back? Junior's hungry and...

              No way. This is our private stockpile for the Ice age.

              Subarctic temperatures will force us underground for a billion years.

              So you got three melons?

              If you weren't smart enough to plan ahead, then doom on you.

              Doom on you. Doom on you. Doom on you.

              - Get away from me. - Doom on you.

              Oh, no. No.

              Retrieve the melon. Tae kwon dodos, attack.

              - The melon. - The melon, the melon, the melon...

              There goes our last female.

              - Got it, got it, got it. - Don't got it.

              The last melon.

              Sid. Now we gotta find more food.

              Right, more to the right. Right, right, right.

              - Look at that. Dinner and a show. - Left, left, left.

              Now to find a meal befitting a conquering hero.

              What ho? A foe? Come on, come on. You want a piece of me?

              Spoils worthy of such a noble...

              Bedtime, squirt.

              The triumphant return.

              Huh? Oh, that.

              I'm so full. How about a good-night kiss for your big buddy, Sid?

              - He's asleep. - I was talking to you.

              Fine, I'll tuck myself in.

              All right, good night.

              Will you stop it?

              All right, all right. I was trying to relax.

              Oy.

              - What the...? - Slice me. It'll be the last thing you ever do.

              - I'm workin' here, you waste of fur. - Frustrated, Diego?

              Tracking down helpless infants too difficult for you?

              - What are you two doin' here? - Soto's getting tired of waiting.

              Yeah, he said "Come back with the baby, or don't come back at all."

              I have a message for Soto.

              Tell him I'm bringing the baby.

              And tell him I'm bringing...

              a mammoth.

              - A mammoth? - Mammoths never travel alone.

              This one does, and I'm leading him to Half Peak.

              Look at all that meat. Let's get him.

              Not yet. We'll need the whole pack to bring this mammoth down.

              Get everyone ready.

              Now.

              - Where's the baby? - You lost it?

              Sid.

              It's so ugly. Positively adorable.

              Hello, pumpkin. Hello, little baldy bean.

              - Where'd you find it? - The poor kid, all alone in the wild.

              Sabers were closing in on him.

              - So I just snatched him. - So brave.

              Yeah, well, he needed me, and I only wish I had one of my own, too.

              Really? I find that attractive in a male.

              Alas. Who wouldn't want a family, I always say.

              - Where've you been hiding? - Yeah, well, you know...

              Cute kid, huh? So, as I was saying, ladies...

              Hey. Hi, Manny.

              What's the matter with you?

              Excuse me, ladies. You just keep marinating and I'll be right back.

              Sexy.

              He's not much to look at, but it's so hard to find a family guy.

              Tell me about it. All the sensitive ones get eaten.

              No, no, no. Manny, please, I'm begging you. I need him.

              - A good-lookin' guy like you? - You say that, but you don't mean it.

              No, seriously. Look at you. Those ladies, they don't stand a chance.

              - You have a cruel sense of humor. - Don't let me cramp your style.

              - Thanks, Manny. You're a pal. - Without Pinky.

              Manny, I need him.

              So, ladies, where were we?

              - Carl. - Easy, Frank.

              Pretty tail walks by, suddenly he moves like a cheetah. And that tiger...

              Yeah, Mr Great Tracker. Can't even find a sloth.

              What am I? The wet nurse? What are you lookin' at, bone bag?

              Look at you. You're gonna grow into a great predator.

              I don't think so. What have you got? You got a little patch of fur.

              No fangs, no claws.

              You're folds of skin wrapped in mush.

              What's so threatening about you?

              Does this look like a petting zoo to you? Huh?

              OK. All right, wise guy. You just earned a time-out.

              You think that's funny? How about this?

              You'll be a little snack for the owls.

              You're a brave little squirt, I'll give you that.

              Thank goodness. Thank goodness. No. A tiger.

              Help. Help.

              - Where's the baby? - Manfred has him.

              Just put me in your mouth. Come on. Hurry up. He got me.

              Help.

              - Get away from me. - It went this way.

              Over here.

              Carl. The tiger beat us to him.

              Wait a minute.

              He's dead all right.

              Carnivores have all the fun.

              Gosh, I hate breaking their hearts like that. But you know how it is.

              All right, thanks. You can put me down now.

              Manny. Manny.

              Guys, I thought we were in a hurry. And Diego, spit that out.

              You don't know where it's been.

              Boy. For a second there, I thought you were gonna eat me.

              - I don't eatjunk food. - Thought you were gonna...

              I thought you were gonna... Were you?

              Come on, wait up. Wait up. Come on, come on. Can you wait a second, please?

              Fellas.

              Thanks for waiting.

              Three, two, one...

              Sure is faithful.

              - Don't make me reach back. - He started it.

              I don't care who started it. I'll finish it.

              Modern architecture. It'll never last.

              Hiya, Manny.

              Hi, Diego.

              Hey, Sid.

              - You're lost. - No. I know exactly where we are.

              Ask him directions.

              - I don't need directions. - Fine, I'll ask him.

              Buddy. You see any humans go by here?

              I love this game. I love this game. OK, OK.

              Three words. First word. Stomp. No, no. Stamp, stamp.

              Let me try. Pack.

              Good one, Manny. Pack of long teeth and claws.

              Pack of wolves? Pack of...

              Pack of bears? Pack of fleas?

              Pack of whiskers? Pack of noses?

              - Pachyderm? - Pack of lies.

              Pack of troubles. Pack a wallop. Pack of birds.

              Pack of flying fish.

              Great news. I found a shortcut.

              - What do you mean, shortcut? - I mean faster than the long way around.

              I know what a shortcut is.

              Either we beat the humans to Glacier Pass or we take the long way and miss 'em.

              Through there? What do you take me for?

              This time tomorrow, you could be a free mammoth.

              Or a nanny. I never get tired of peekaboo.

              Guys. Guys. Check this out.

              Sid, the tiger found a shortcut.

              No, thanks. I choose life.

              Then I suggest you take the shortcut.

              - Are you threatening me? - Move, sloth!

              Way to go, tiger.

              Quick. Get inside.

              OK, I vote shortcut.

              Guys, stick together. It's easy to get lost in here.

              Guys?

              A fish.

              Will you keep up, please? Hard enough to keep track of one baby.

              I gotcha.

              Captain, iceberg ahead.

              Oh, no.

              Yeah. Who's up for round two?

              Tell the kid to be more careful.

              Look, look. Tigers.

              No, it's OK, it's OK. Look, the tigers are just playing tag with the antelope.

              - With their teeth. - Come on, Sid, let's play tag.

              You're it.

              Sure. OK, OK, OK, where are the sloths?

              You never see any sloths. Have you ever noticed?

              - Look, Manny, a mammoth. - Somebody pinch me.

              Hey, hey, this fat one looks just like you.

              And he's got a family.

              And he's happy. Look, he's playing with his kid.

              See? That's your problem. That's what mammoths are supposed to do.

              - Sid... - Find a she-mmoth, have baby mammoths...

              - Sid. - What?

              - Shut up. - But...

              Would you look at that.

              The tiger actually did it. There's Half Peak.

              Next stop, Glacier Pass.

              - How could I ever have doubted you? - Did you hear that, little fella?

              You're almost home.

              - My feet are sweating. - Do we need a news flash

              - every time your body does something? - Ignore him.

              Seriously. My feet are really hot.

              Tell me that was your stomach.

              I'm sure it was just thunder.

              From underground?

              Come on, keep up with me.

              I would if you were moving.

              - I wish I could jump like that. - Wish granted.

              - Come on, move faster. - Have you noticed the river of lava?

              Hold Pinky.

              Manny.

              Manny, Manny, Manny, you OK? Come on, come on, say something. Anything.

              What? What? I can't hear you.

              You're standing on my trunk.

              - You're OK. You're OK. - Why did you do that?

              You could have died, trying to save me.

              That's what you do in a herd.

              You look out for each other.

              Well, thanks.

              I don't know about you guys, but we are the weirdest herd I've ever seen.

              I can't wait to get my claws in that mammoth.

              No one touches the mammoth until I get that baby.

              First, I'll slice its hindquarters into sections.

              - I'll put the white meat in one pile and... - Knock it off. I'm starving.

              Next, the shoulders. Occasionally tough, but extremely juicy.

              - I told you to knock it off. - Save your energy.

              Mammoths don't go down easy.

              There's only one way to do it.

              First, you have to force it into a corner.

              Cut off its retreat. And when you three have it trapped,

              I'll go for the throat.

              Guys, we gotta get this kid outta the wind.

              - How much further? - Three miles.

              I'm beat. We'll get there in the morning.

              - What are you doin'? - I'm putting sloths on the map.

              Why don't you make it realistic and draw him lying down?

              And make him rounder.

              - Perfect. - I forgot how to laugh.

              I'm a genius.

              From now on you'll have to refer to me as Sid, Lord of the Flame.

              Lord of the Flame, your tail's on fire.

              Thank you. From now on, I'm gonna call you Diego.

              Lord of "Touch Me and You're Dead".

              I'm just kiddin', you little knucklehead.

              Lovebirds.

              Look at this.

              I don't believe it.

              Come here, you little biped. Come here, you little wormy-worm.

              Come to Uncle Sid.

              No, no, no, no, no, no. This way. This way.

              No, no, no. No, go to him.

              Go to him.

              OK.

              Good job. Keep practicin'.

              Look at that. Our little guy is growing up.

              All right, come on. Sleep time, lumpy.

              Look at that big pushover.

              You know, Diego, I've never had a friend who would risk his life for me.

              Yeah, Manny's... he's a good guy.

              Yeah, he is.

              Well, good night.

              Let's get you all cleaned up. What's your daddy gonna say if you go back all stinky?

              Let me just clean that up. That looks good. A little bit here.

              - You clean up nice, little fella. - I think he's starting to look like me.

              Diego, what do you think?

              - Maybe we shouldn't do this. - Why not?

              If we save him, he'll be a hunter. And who do you think he'll hunt?

              Maybe because we save him, he won't hunt us.

              Yeah, and maybe he'll grow fur and a long skinny neck and call you Mama.

              - What's your problem? - Nothing. Let's go. I'm freezing my tail off.

              Diego. You frozen back there?

              - Get down. - What?

              - Get down and follow me. - What's goin' on?

              At the bottom of Half Peak, there's an ambush waiting for you.

              - What? - What do you mean, "ambush"?

              - You set us up. - It was my job.

              - I was to get the baby, but then... - You brought us home for dinner.

              - That's it. You're out of the herd. - I'm sorry.

              No, you're not. Not yet.

              - Listen, I can help you. - Stay close, Sid. We can fight our way out.

              You can't. The pack's too strong. You have to trust me.

              Trust you? Why in the world would we trust you?

              Because I'm your only chance.

              Hello, ladies.

              - Look who decided to show up. - Diego, I was beginning to worry about you.

              No need to worry. In about two minutes you'll be satisfying your taste for revenge.

              Very nice.

              I see the sloth. And he's got the baby.

              Don't give away your positions until you see the mammoth. He's the one to surprise.

              You want to maul something, don't you?

              - I wanna maul. - Then what are you waiting for?

              No, I said wait for the mammoth.

              Backscratcher. Eat my powder!

              Loop-de-loop.

              Slalom! Slalom, baby!

              Sorry, fellas. He got a little frostbite.

              Get him.

              Surprise!

              OK, follow me. We'll pick up Sid and get outta here while we can.

              Come on, Diego, let's bring this mammoth down.

              There he is.

              That's right. Where's the baby?

              Survival of the fittest.

              I don't think so. Yeah.

              - What are you doing? - Leave the mammoth alone.

              Fine. I'll take you down first.

              We did it.

              We were some team, huh?

              Were? Come on, we're still a team.

              I'm sorry I set you up.

              You know me - I'm too lazy to hold a grudge.

              Knock it off, squirt.

              You gotta be strong.

              You have to take care of Manfred and Sid.

              Especially Sid.

              Come on, you can lick this. You're a tiger.

              Look, I'll carry you. Come on, what do you say?

              Come on, Diego, come on.

              Tell him he's going to be OK, Manny.

              Listen, you have to leave me here.

              If those humans get through the pass, you'll never catch them.

              You didn't have to do that.

              That's what you do in a herd.

                Don't forget about us.

                OK?

                We won't forget about you.

                Goodbye.

                Goodbye. Goodbye.

                - Sid... - Bye.

                Bye.

                That's right. Where's the baby?

                Come on, Sid, let's head south.

                Bye.

                Save your breath, Sid. You know humans can't talk.

                Diego? You're OK.

                - Nine lives, baby. - You're OK.

                You're OK.

                I could kiss ya.

                Welcome back, partner. Wanna lift?

                No thanks. I gotta save whatever dignity I've got left.

                You're hanging out with us. Dignity's got nothing to do with it. I'll take that lift.

                - Yeah, climb aboard. - Pick me up, buddy.

                Mush. Or not mush. Either way.

                This is gonna be the best migration ever.

                I'll show you my favorite watering holes. I turn brown when the fungus in my fur dries.

                - Attractive. - This whole Ice age thing is getting old.

                You know what I could go for? Global warming.

                - Keep dreamin'. - No, really...
*/