public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    private static double G = 6.67e-11;
    public Planet(Planet b){
        xxPos=b.xxPos;
        yyPos=b.yyPos;
        xxVel=b.xxVel;
        yyVel=b.yyVel;
        mass=b.mass;
        imgFileName=b.imgFileName;
    }
    public Planet(double xP, double yP, double xV,double yV, double m, String img){
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }
    public double calcDistance(Planet b){
        double xd=xxPos-b.xxPos;
        double yd=yyPos-b.yyPos;
        return Math.sqrt(xd*xd+yd*yd);
    }
    public double calcForceExertedBy(Planet b){
        double dis=calcDistance(b);
        return G*mass*b.mass/Math.pow(dis, 2);
    }
    public double calcForceExertedByX(Planet b){
        double F=calcForceExertedBy(b);
        return F*(b.xxPos-xxPos)/calcDistance(b);
    }
    public double calcForceExertedByY(Planet b){
        double F=calcForceExertedBy(b);
        return F*(b.yyPos-yyPos)/calcDistance(b);
    }
    public double calcNetForceExertedByX(Planet a[]){
        double ans=0;
        for(Planet p:a){
            if(this.equals(p)==false){
                ans+=calcForceExertedByX(p);
            }
        }
        return ans;
    }
    public double calcNetForceExertedByY(Planet a[]){
        double ans=0;
        for(int i=0;i<a.length;i++){
            if(this.equals(a[i])==false){
                ans+=calcForceExertedByY(a[i]);
            }
        }
        return ans;
    }
    public void update(double dt,double fx, double fy){
        xxVel= xxVel+fx/mass*dt;
        yyVel= yyVel+fy/mass*dt;
        xxPos=xxPos+xxVel*dt;
        yyPos=yyPos+yyVel*dt;
    }
    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
    }
}
