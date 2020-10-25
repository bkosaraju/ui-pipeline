import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { GlobalConfigUpdateComponent } from 'app/entities/global-config/global-config-update.component';
import { GlobalConfigService } from 'app/entities/global-config/global-config.service';
import { GlobalConfig } from 'app/shared/model/global-config.model';

describe('Component Tests', () => {
  describe('GlobalConfig Management Update Component', () => {
    let comp: GlobalConfigUpdateComponent;
    let fixture: ComponentFixture<GlobalConfigUpdateComponent>;
    let service: GlobalConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [GlobalConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(GlobalConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GlobalConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GlobalConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GlobalConfig(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new GlobalConfig();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
